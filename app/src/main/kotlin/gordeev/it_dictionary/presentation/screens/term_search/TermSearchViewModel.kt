package gordeev.it_dictionary.presentation.screens.term_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val loadingState = MutableStateFlow(false)
    private val errorState = MutableStateFlow(false)

    val state = combine(
        searchQuery,
        loadingState,
        errorState
    ) { searchQuery, isLoading, isError ->
        TermSearchViewState(
            searchValue = searchQuery,
            isLoading = isLoading,
            isError = isError
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TermSearchViewState()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private var searchResultPagedList = searchQuery.flatMapLatest {
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
    }

    companion object {
        private val PAGING_CONFIG = PagingConfig(
            pageSize = 12,
            initialLoadSize = 16,
            prefetchDistance = 0
        )
    }
}