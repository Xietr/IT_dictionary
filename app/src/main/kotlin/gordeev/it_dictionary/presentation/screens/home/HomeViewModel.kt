package gordeev.it_dictionary.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import gordeev.it_dictionary.data.data_sources.local.entities.result.TermSetWithTerms
import gordeev.it_dictionary.data.repositories.DictionaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dictionaryRepository: DictionaryRepository
) : ViewModel() {

    val pagedList: Flow<PagingData<TermSetWithTerms>> =
        dictionaryRepository.termSetWithTermsPagingSource(PAGING_CONFIG).cachedIn(viewModelScope)

    private var termSetWithTerms: TermSetWithTerms? = null
        set(value) {
            selectedTermSetIdState.value = value?.termSet?.id
            field = value
        }
    private var selectedTermSetIdState = MutableStateFlow<String?>(null)
    val selectedTermSetId: StateFlow<String?> = selectedTermSetIdState

    fun addTermSetToFavorite() {
        termSetWithTerms?.let {
            viewModelScope.launch {
                dictionaryRepository.setTermsAreFavorite(it.terms.associate { it.termId to true })
            }
        }
    }

    fun onOpenDialog(termSetWithTerms: TermSetWithTerms) {
        this.termSetWithTerms = termSetWithTerms
    }

    fun onCloseDialog() {
        termSetWithTerms = null
    }

    companion object {
        private val PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            initialLoadSize = 14,
        )
    }
}