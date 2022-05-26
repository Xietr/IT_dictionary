package gordeev.it_dictionary.data.utils

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import gordeev.it_dictionary.data.data_sources.local.daos.DictionaryDao
import gordeev.it_dictionary.data.data_sources.local.entities.result.TermSet.Companion.fromRemoteTermSet
import gordeev.it_dictionary.data.data_sources.local.entities.result.TermSetWithTerms
import gordeev.it_dictionary.data.data_sources.local.entities.update.UpdateTermWithRemoteData.Companion.fromRemoteTerm
import gordeev.it_dictionary.data.data_sources.remote.DictionaryRemoteDataSource

@OptIn(ExperimentalPagingApi::class)
class DictionaryRemoteMediator(
    private val dictionaryDao: DictionaryDao,
    private val dictionaryRemoteDataSource: DictionaryRemoteDataSource
) : RemoteMediator<Int, TermSetWithTerms>() {

    companion object {
        private const val ITEMS_TO_LOAD = 10
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TermSetWithTerms>
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

            val response = dictionaryRemoteDataSource.getDictionaryPart(
                startKey = loadKey, limit = ITEMS_TO_LOAD
            )

            response.forEach { remoteTermSet ->
                dictionaryDao.insertTermSet(remoteTermSet.fromRemoteTermSet())
                remoteTermSet.terms.forEach { remoteTerm ->
                    dictionaryDao.insertOrUpdateTerm(remoteTerm.fromRemoteTerm(remoteTermSet.id))
                }
            }

            MediatorResult.Success(endOfPaginationReached = response.size < ITEMS_TO_LOAD)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}