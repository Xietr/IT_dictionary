@file:OptIn(ExperimentalPagingApi::class)

package gordeev.it_dictionary.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import gordeev.it_dictionary.data.data_sources.local.daos.DictionaryDao
import gordeev.it_dictionary.data.data_sources.local.entities.result.TermSetWithTerm
import gordeev.it_dictionary.data.data_sources.local.entities.result.TermSetWithTerms
import gordeev.it_dictionary.data.data_sources.local.entities.update.UpdateTermIsFavorite
import gordeev.it_dictionary.data.data_sources.remote.DictionaryRemoteDataSource
import gordeev.it_dictionary.data.data_sources.remote.entities.requests.RequestToAddTerm
import gordeev.it_dictionary.data.data_sources.utils.InvokeStatus
import gordeev.it_dictionary.data.utils.DictionaryRemoteMediator
import gordeev.it_dictionary.data.utils.DictionarySearchRemoteMediator
import kotlinx.coroutines.CoroutineStart.LAZY
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DictionaryRepositoryImpl @Inject constructor(
    private val dictionaryRemoteDataSource: DictionaryRemoteDataSource,
    private val dictionaryDao: DictionaryDao
) : DictionaryRepository {

    override fun termSetWithTermsPagingSource(pagingConfig: PagingConfig): Flow<PagingData<TermSetWithTerms>> =
        Pager(
            config = pagingConfig,
            remoteMediator = DictionaryRemoteMediator(dictionaryDao, dictionaryRemoteDataSource),
            pagingSourceFactory = { dictionaryDao.getDictionaryPagingSource() }
        ).flow.flowOn(Dispatchers.IO)

    override suspend fun toggleFavorite(termSetId: String) {
        TODO("Not yet implemented")
    }

    override fun sendRequestToAddTerm(name: String, meaning: String, termSetName: String): Flow<InvokeStatus> =
        dictionaryRemoteDataSource.sendRequestToAddTerm(RequestToAddTerm(name, meaning, termSetName))

    @OptIn(DelicateCoroutinesApi::class)
    val allTermSets = GlobalScope.async(Dispatchers.IO, start = LAZY) { dictionaryRemoteDataSource.getAllTermSetsName() }

    override fun observableTermSetsByName(name: String): Flow<List<String>> =
        flow {
            emit(
                allTermSets.await().filter { it.contains(name) }
            )
        }

    override suspend fun setTermIsFavorite(termId: String, isFavorite: Boolean) {
        dictionaryDao.updateTermIsFavorite(UpdateTermIsFavorite(termId, isFavorite))
    }

    @Throws(Exception::class)
    override fun termSetWithTermsPagingSourceByTermName(pagingConfig: PagingConfig, termNameQuery: String): Flow<PagingData<TermSetWithTerm>> =
        Pager(
            config = pagingConfig,
            remoteMediator = DictionarySearchRemoteMediator(termNameQuery, dictionaryDao, dictionaryRemoteDataSource),
            pagingSourceFactory = { dictionaryDao.getDictionaryPagingSourceByTerm(termNameQuery) }
        ).flow.flowOn(Dispatchers.IO)
}