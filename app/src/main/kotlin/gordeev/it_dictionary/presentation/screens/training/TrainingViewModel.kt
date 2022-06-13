package gordeev.it_dictionary.presentation.screens.training

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gordeev.it_dictionary.data.repositories.DictionaryRepository
import gordeev.it_dictionary.presentation.trainingArg
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    currentTrainingTermSetWithTerms: CurrentTrainingTermSetWithTerms,
    private val dictionaryRepository: DictionaryRepository
) : ViewModel() {

    private val pageIndex: Int = savedStateHandle.get(trainingArg)!!
    private val termSetWithTerms = currentTrainingTermSetWithTerms.termSetWithTerms
    private val term = termSetWithTerms.terms[pageIndex]

    val state = TrainingScreenState(
        term.termName,
        term.meaning,
        pageIndex + 1,
        termSetWithTerms.terms.size
    )

    fun setTermIsLearned(learned: Boolean) {
        viewModelScope.launch {
            dictionaryRepository.setTermIsLearned(term.termId, learned)
        }
    }
}