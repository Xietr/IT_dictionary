package gordeev.it_dictionary.presentation.screens.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gordeev.it_dictionary.data.data_sources.local.entities.result.TermSetWithTerms
import gordeev.it_dictionary.data.repositories.DictionaryRepository
import gordeev.it_dictionary.presentation.screens.favorite.FavoriteScreenTab.LEARNED
import gordeev.it_dictionary.presentation.screens.favorite.FavoriteScreenTab.TO_LEARN
import gordeev.it_dictionary.presentation.screens.training.CurrentTrainingTermSetWithTerms
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    dictionaryRepository: DictionaryRepository,
    private val currentTrainingTermSetWithTerms: CurrentTrainingTermSetWithTerms
) : ViewModel() {

    private val currentTab = MutableStateFlow(TO_LEARN)

    private val favoriteTermSetsWithTerms = dictionaryRepository.getAllFavoriteTermSetsWithTerms()

    val state = combine(currentTab, favoriteTermSetsWithTerms) { tab, termSetsWithTerms ->
        FavoriteScreenState(
            tab,
            if (tab == LEARNED) {
                termSetsWithTerms.apply {
                    this.map {
                        it.copy(
                            terms = it.terms.filter { term -> term.isLearned }
                        )
                    }
                }.filter { it.terms.isNotEmpty() }
            } else termSetsWithTerms
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FavoriteScreenState()
    )

    fun changeTab(tab: FavoriteScreenTab) {
        currentTab.value = tab
    }

    fun onOpenTrainingScreen(termSetWithTerms: TermSetWithTerms) {
        currentTrainingTermSetWithTerms.termSetWithTerms = termSetWithTerms
    }
}