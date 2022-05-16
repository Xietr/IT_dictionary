package gordeev.it_dictionary.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import gordeev.it_dictionary.data.data_sources.local.entities.TermSet
import gordeev.it_dictionary.data.utils.PaginatedEntityRemoteMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Provider

class DictionaryRepositoryImpl @Inject constructor(
    private val dictionaryRemoteDataSource: Provider<PagingSource<String, TermSet>>,
) : DictionaryRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getDictionaryPart(pagingConfig: PagingConfig): Flow<PagingData<TermSet>> =
        Pager(
            config = pagingConfig,
            remoteMediator = PaginatedEntityRemoteMediator(),
            pagingSourceFactory = dictionaryRemoteDataSource::get
        ).flow.flowOn(Dispatchers.IO)

    override suspend fun toggleFavorite(termSetId: String) {
        TODO("Not yet implemented")
    }
}