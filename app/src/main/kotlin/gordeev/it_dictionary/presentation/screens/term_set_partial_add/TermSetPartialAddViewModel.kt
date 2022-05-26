package gordeev.it_dictionary.presentation.screens.term_set_partial_add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gordeev.it_dictionary.data.data_sources.local.daos.DictionaryDao
import gordeev.it_dictionary.data.data_sources.local.entities.result.Term
import gordeev.it_dictionary.data.data_sources.local.entities.update.UpdateTermIsFavorite
import gordeev.it_dictionary.presentation.termSetPartialAddArg
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermSetPartialAddViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val dictionaryDao: DictionaryDao
) : ViewModel() {
    private val termSetId: String = savedStateHandle.get(termSetPartialAddArg)!!

    private val selectedTermSetsIdToFavorite = MutableStateFlow(mapOf<String, Boolean>())

    val termSet = combine(
        dictionaryDao.getTermSetObservableById(termSetId),
        selectedTermSetsIdToFavorite
    ) { termSet, selected ->
        termSet?.let {
            it.copy(
                terms = it.terms.map { term ->
                    selected[term.termId]?.let { isFavorite ->
                        term.copy(isFavorite = isFavorite)
                    } ?: term
                }
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = null
    )

    fun onAddWordsClicked() {
        viewModelScope.launch {
            dictionaryDao.updateTermsAreFavorite(selectedTermSetsIdToFavorite.value.map { UpdateTermIsFavorite(it.key, it.value) })
        }
    }

    fun onFavoriteChanged(term: Term, isFavorite: Boolean) {
        selectedTermSetsIdToFavorite.value = selectedTermSetsIdToFavorite.value.toMutableMap().apply {
            put(term.termId, isFavorite)
        }
    }
}