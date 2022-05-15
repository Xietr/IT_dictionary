package gordeev.it_dictionary.data.utils

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import gordeev.it_dictionary.data.local.entities.PaginatedEntity

@OptIn(ExperimentalPagingApi::class)
internal class PaginatedEntityRemoteMediator<E : PaginatedEntity>(
    private val fetch: suspend (lastId: String?) -> Unit
) : RemoteMediator<String, E>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<String, E>
    ): MediatorResult {
        val nextPage = when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull() ?: return MediatorResult.Success(endOfPaginationReached = true)
                lastItem.pageStartKey
            }
        }
        return try {
            fetch(nextPage)
            MediatorResult.Success(endOfPaginationReached = false)
        } catch (t: Throwable) {
            MediatorResult.Error(t)
        }
    }
}