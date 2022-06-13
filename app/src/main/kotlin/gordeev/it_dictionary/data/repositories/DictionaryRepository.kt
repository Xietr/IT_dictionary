package gordeev.it_dictionary.data.repositories

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import gordeev.it_dictionary.data.data_sources.local.entities.result.TermSetWithTerm
import gordeev.it_dictionary.data.data_sources.local.entities.result.TermSetWithTerms
import gordeev.it_dictionary.data.data_sources.utils.InvokeStatus
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {

    fun termSetWithTermsPagingSource(pagingConfig: PagingConfig): Flow<PagingData<TermSetWithTerms>>

    fun sendRequestToAddTerm(name: String, meaning: String, termSetName: String): Flow<InvokeStatus>

    fun observableTermSetsByName(name: String): Flow<List<String>>

    fun observableTermSetById(id: String): Flow<TermSetWithTerms>

    suspend fun setTermIsFavorite(termId: String, isFavorite: Boolean)

    suspend fun setTermsAreFavorite(termIdToIsFavorite: Map<String, Boolean>)

    suspend fun setTermIsLearned(termId: String, isLearned: Boolean)

    @Throws(Exception::class)
    fun termSetWithTermsPagingSourceByTermName(
        pagingConfig: PagingConfig,
        termNameQuery: String
    ): Flow<PagingData<TermSetWithTerm>>

    fun getAllFavoriteTermSetsWithTerms(): Flow<List<TermSetWithTerms>>
}