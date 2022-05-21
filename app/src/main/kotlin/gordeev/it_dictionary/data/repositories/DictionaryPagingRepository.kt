package gordeev.it_dictionary.data.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import gordeev.it_dictionary.data.data_sources.local.daos.DictionaryDao
import gordeev.it_dictionary.data.data_sources.local.entities.TermSet
import gordeev.it_dictionary.data.data_sources.local.entities.TermSet.Companion.fromRemoteTermSet
import gordeev.it_dictionary.data.data_sources.remote.DictionaryRemoteDataSource
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

class DictionaryPagingRepository @Inject constructor(
    private val dictionaryRemoteDataSource: DictionaryRemoteDataSource,
    private val dictionaryDao: DictionaryDao
) : PagingSource<String, TermSet>() {

    override fun getRefreshKey(state: PagingState<String, TermSet>) = null

    @OptIn(FlowPreview::class)
    override suspend fun load(params: LoadParams<String>): LoadResult<String, TermSet> {
        val loadSize = params.loadSize
        val databaseDictionary = (params.key?.let {
            dictionaryDao.getDictionaryPart(it, loadSize)
        } ?: dictionaryDao.getDictionaryPart(loadSize))

        //if size of termSets from database is less than required from params
        return if (databaseDictionary.size < params.loadSize) {
            val loadResult = kotlin.runCatching {
                dictionaryRemoteDataSource.getDictionaryPart(params.key, params.loadSize)
            }
            //if error from remote and we have termSets from database return them as success
            if (loadResult.isFailure && databaseDictionary.isNotEmpty()) {
                return LoadResult.Page(
                    data = databaseDictionary,
                    prevKey = null,
                    nextKey = databaseDictionary.last().id
                )
            }

            //if request from remote is successful map its' isFavorite to database isFavorite else just return error
            @Suppress("UNCHECKED_CAST")
            loadResult.getOrElse {
                return LoadResult.Error(it)
            }.let { remoteTermSets ->
                val termSet = remoteTermSets.map {
                    it.fromRemoteTermSet(
                        isFavorite = dictionaryDao.getTermSet(it.id)?.isFavorite ?: false
                    )
                }
                dictionaryDao.insertAll(termSet)
                LoadResult.Page(
                    data = termSet,
                    prevKey = null,
                    nextKey = termSet.lastOrNull()?.id
                )
            }
        } else {
            return LoadResult.Page(
                data = databaseDictionary,
                prevKey = null,
                nextKey = databaseDictionary.last().id
            )
        }
    }
}