package gordeev.it_dictionary.presentation.screens.training

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import gordeev.it_dictionary.presentation.trainingArg
import javax.inject.Inject

@HiltViewModel
class TrainingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val currentTrainingTermSetWithTerms: CurrentTrainingTermSetWithTerms
) : ViewModel() {
    val termSetId: Int = savedStateHandle.get(trainingArg)!!

    init {
        println("Instance $this + $termSetId")
    }
}