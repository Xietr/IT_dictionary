package gordeev.it_dictionary.data

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import gordeev.it_dictionary.data.local.entities.TermSet
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {

    fun getDictionaryPart(pagingConfig: PagingConfig): Flow<PagingData<TermSet>>

    suspend fun toggleFavorite(termSetId: String)
}