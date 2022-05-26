package gordeev.it_dictionary.data.utils

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import gordeev.it_dictionary.data.data_sources.local.daos.DictionaryDao
import gordeev.it_dictionary.data.data_sources.local.entities.result.TermSet.Companion.fromRemoteTermSet
import gordeev.it_dictionary.data.data_sources.local.entities.result.TermSetWithTerm
import gordeev.it_dictionary.data.data_sources.local.entities.update.UpdateTermWithRemoteData.Companion.fromRemoteTerm
import gordeev.it_dictionary.data.data_sources.remote.DictionaryRemoteDataSource
import kotlinx.coroutines.CoroutineStart.LAZY
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

@OptIn(ExperimentalPagingApi::class)
class DictionarySearchRemoteMediator(
    private val termNameQuery: String,
    private val dictionaryDao: DictionaryDao,
    private val dictionaryRemoteDataSource: DictionaryRemoteDataSource
) : RemoteMediator<Int, TermSetWithTerm>() {

    //firebase do not support queries so we have to get the whole dictionary :(
    @OptIn(DelicateCoroutinesApi::class)
    private val dictionary = GlobalScope.async(Dispatchers.IO, start = LAZY) {
        dictionaryRemoteDataSource.getDictionaryPart(limit = Int.MAX_VALUE)
    }

    companion object {
        private const val ITEMS_TO_LOAD = 10
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TermSetWithTerm>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull() ?: return MediatorResult.Success(endOfPaginationReached = true)
                    lastItem.termSet.id
                }
            }

            //emulating search
            val response = dictionary
                .await()
                .asSequence()
                .dropWhile {
                    if (loadKey != null) {
                        it.id != loadKey
                    } else false
                }
                .filter {
                    it.terms.map {
                        it.name.contains(termNameQuery)
                    }.isNullOrEmpty().not()
                }
                .take(ITEMS_TO_LOAD)

            response.forEach { remoteTermSet ->
                dictionaryDao.insertTermSet(remoteTermSet.fromRemoteTermSet())
                remoteTermSet.terms.forEach { remoteTerm ->
                    dictionaryDao.insertOrUpdateTerm(remoteTerm.fromRemoteTerm(remoteTermSet.id))
                }
            }

            MediatorResult.Success(endOfPaginationReached = response.count() < ITEMS_TO_LOAD)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}