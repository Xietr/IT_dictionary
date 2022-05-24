package gordeev.it_dictionary.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import gordeev.it_dictionary.data.data_sources.invokeStatus.InvokeStatus
import gordeev.it_dictionary.data.data_sources.local.entities.TermSet
import gordeev.it_dictionary.data.data_sources.remote.DictionaryRemoteDataSource
import gordeev.it_dictionary.data.data_sources.remote.entities.requests.RequestToAddTerm
import gordeev.it_dictionary.data.utils.PaginatedEntityRemoteMediator
import kotlinx.coroutines.CoroutineStart.LAZY
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Provider

class DictionaryRepositoryImpl @Inject constructor(
    private val termSetPagingSourceProvider: Provider<PagingSource<String, TermSet>>,
    private val dictionaryRemoteDataSource: DictionaryRemoteDataSource
) : DictionaryRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getDictionaryPart(pagingConfig: PagingConfig): Flow<PagingData<TermSet>> =
        Pager(
            config = pagingConfig,
            remoteMediator = PaginatedEntityRemoteMediator(),
            pagingSourceFactory = termSetPagingSourceProvider::get
        ).flow.flowOn(Dispatchers.IO)

    override suspend fun toggleFavorite(termSetId: String) {
        TODO("Not yet implemented")
    }

    override fun sendRequestToAddTerm(name: String, meaning: String, termSetName: String): Flow<InvokeStatus> =
        dictionaryRemoteDataSource.sendRequestToAddTerm(RequestToAddTerm(name, meaning, termSetName))

    @OptIn(DelicateCoroutinesApi::class)
    val allTermSets = GlobalScope.async(Dispatchers.IO, start = LAZY) { dictionaryRemoteDataSource.getAllTermSetsName() }

    override fun getTermSetsByName(name: String): Flow<List<String>> =
        flow {
            emit(
                allTermSets.await().filter { it.contains(name) }
            )
        }
}