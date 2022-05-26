package gordeev.it_dictionary.data.utils

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import gordeev.it_dictionary.data.data_sources.local.entities.result.PaginatedEntity

@OptIn(ExperimentalPagingApi::class)
internal class PaginatedEntityRemoteMediator<E : PaginatedEntity> : RemoteMediator<String, E>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<String, E>
    ): MediatorResult {
        return if (state.lastItemOrNull() == null)
            MediatorResult.Success(endOfPaginationReached = true)
        else
            MediatorResult.Success(endOfPaginationReached = false)
    }
}