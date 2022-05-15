package gordeev.it_dictionary.data.remote.firebase

import androidx.paging.PagingState
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import gordeev.it_dictionary.data.DictionaryPagingDataSource
import gordeev.it_dictionary.data.remote.entities.RemoteTermSet
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseDictionaryPagingDataSource @Inject constructor() : DictionaryPagingDataSource() {

    private val database = Firebase.database

    override fun getRefreshKey(state: PagingState<String, RemoteTermSet>): String? =
        state.anchorPosition?.let { state.closestItemToPosition(it)?.id }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, RemoteTermSet> {
        return try {
            val currentPage = database
                .getReference(DICTIONARY_PATH)
                .orderByKey()
                .run {
                    params.key?.let {
                        startAfter(it)
                    } ?: this
                }
                .limitToFirst(params.loadSize)
                .get()
                .await()
            val nextPage = currentPage.children.last().key
            LoadResult.Page(
                data = currentPage
                    .children
                    .mapNotNull {
                        it.getValue(RemoteTermSet::class.java)?.copy(id = it?.key ?: "")
                    },
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val DICTIONARY_PATH = "dictionary"
    }
}