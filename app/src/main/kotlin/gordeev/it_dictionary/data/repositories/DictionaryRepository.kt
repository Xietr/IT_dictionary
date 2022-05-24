package gordeev.it_dictionary.data.repositories

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import gordeev.it_dictionary.data.data_sources.invokeStatus.InvokeStatus
import gordeev.it_dictionary.data.data_sources.local.entities.TermSet
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {

    fun getDictionaryPart(pagingConfig: PagingConfig): Flow<PagingData<TermSet>>

    suspend fun toggleFavorite(termSetId: String)

    fun sendRequestToAddTerm(name: String, meaning: String, termSetName: String): Flow<InvokeStatus>

    fun getTermSetsByName(name: String): Flow<List<String>>
}