package gordeev.it_dictionary.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import gordeev.it_dictionary.data.local.daos.DictionaryDao
import gordeev.it_dictionary.data.local.entities.TermSet
import gordeev.it_dictionary.data.local.entities.TermSet.Companion.fromRemoteTermSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DictionaryRepositoryImpl @Inject constructor(
    private val dictionaryPagingDataSource: DictionaryPagingDataSource,
    private val dictionaryDao: DictionaryDao
) : DictionaryRepository {

    companion object {
        //do not modify all below or users will not get previously cached first page properly
        private const val FIRST_PAGE_START_KEY = "key"
        private const val PAGE_SIZE = 20
    }

    //    private val dictionaryStore = StoreBuilder.from(
    //        fetcher = Fetcher.of { pageStartKey: String ->
    //            dictionaryPagingDataSource.getDictionaryPart(pageStartKey.takeIf { it != FIRST_PAGE_START_KEY }, PAGE_SIZE)
    //        },
    //        sourceOfTruth = SourceOfTruth.of(
    //            reader = { pageStartKey ->
    //                dictionaryDao.dictionaryObservable(pageStartKey).map {
    //                    if (it.isNullOrEmpty()) null
    //                    else it
    //                }
    //            },
    //            writer = { pageStartKey, dictionaryPart ->
    //                dictionaryDao.insertAll(
    //                    dictionaryPart.map { it.fromRemoteTermSet(pageStartKey = pageStartKey) }
    //                )
    //            }
    //        )
    //    ).build()

    @OptIn(ExperimentalPagingApi::class)
    override fun getDictionaryPart(pagingConfig: PagingConfig): Flow<PagingData<TermSet>> =
        Pager(
            config = pagingConfig,
            pagingSourceFactory = { dictionaryPagingDataSource }
        ).flow.flowOn(Dispatchers.IO).map { it.map { it.fromRemoteTermSet() } }

    override suspend fun toggleFavorite(termSetId: String) {
        TODO("Not yet implemented")
    }
}