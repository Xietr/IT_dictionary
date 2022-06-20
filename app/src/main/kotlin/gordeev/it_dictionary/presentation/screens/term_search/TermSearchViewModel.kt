package gordeev.it_dictionary.presentation.screens.term_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import gordeev.it_dictionary.data.data_sources.InvokeError
import gordeev.it_dictionary.data.data_sources.InvokeSuccess
import gordeev.it_dictionary.data.data_sources.local.entities.result.TermSetWithTerm
import gordeev.it_dictionary.data.repositories.DictionaryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class TermSearchViewModel @Inject constructor(
    private val dictionaryRepository: DictionaryRepository
) : ViewModel() {
    private val searchQuery = MutableStateFlow("")
    private val successState = MutableStateFlow(false)
    private val loadingState = MutableStateFlow(false)
    private val errorState = MutableStateFlow(false)
    private val secretActivatedState = MutableStateFlow(false)

    val state = combine(
        searchQuery,
        secretActivatedState,
        successState,
        loadingState,
        errorState
    ) { searchQuery, isSecretActivated, isSuccess, isLoading, isError ->
        TermSearchViewState(
            searchValue = searchQuery,
            isSecretActivated = isSecretActivated,
            isSuccess = isSuccess,
            isLoading = isLoading,
            isError = isError
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TermSearchViewState()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val searchResultPagedList = searchQuery.flatMapLatest {
        if (it.contains(SECRET_KEY, ignoreCase = true)) {
            dictionaryRepository.saveSecretToFavorite().collect { invokeStatus ->
                errorState.value = invokeStatus is InvokeError
                if (invokeStatus is InvokeSuccess) {
                    secretActivatedState.value = true
                }
            }
        }
        dictionaryRepository
            .termSetWithTermsPagingSourceByTermName(PAGING_CONFIG, it)
    }.cachedIn(viewModelScope)
    private val selectedTerms = MutableStateFlow(mapOf<String, Boolean>())

    val pagingTermSetWithTermFlow: Flow<PagingData<TermSetWithTerm>> =
        searchResultPagedList.combine(selectedTerms) { searchResultPagedList, selectedTermSetsToTerm ->
            searchResultPagedList.map { termSetWithTerm ->
                selectedTermSetsToTerm[termSetWithTerm.term.termId]?.let {
                    termSetWithTerm.copy(
                        term = termSetWithTerm.term.copy(
                            isFavorite = it
                        )
                    )
                } ?: termSetWithTerm
            }
        }

    fun search(text: String) {
        searchQuery.value = text
    }

    fun onFavoriteChanged(termSetWithTerm: TermSetWithTerm, isFavorite: Boolean) {
        selectedTerms.value = selectedTerms.value.toMutableMap().apply {
            put(termSetWithTerm.term.termId, isFavorite)
        }
    }

    fun onAddWordsClicked() {
        viewModelScope.launch {
            selectedTerms.value.forEach {
                dictionaryRepository.setTermIsFavorite(it.key, it.value)
            }
        }
        successState.value = true
        successState.value = false
    }

    fun onSecretDialogHide() {
        secretActivatedState.value = false
    }

    companion object {
        private val PAGING_CONFIG = PagingConfig(
            pageSize = 12,
            initialLoadSize = 16,
            prefetchDistance = 0
        )
        private const val SECRET_KEY = "ркси"
    }
}