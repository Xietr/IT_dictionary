package gordeev.it_dictionary.presentation.screens.term_set_partial_add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import gordeev.it_dictionary.presentation.termSetPartialAddArg
import javax.inject.Inject

@HiltViewModel
class TermSetPartialAddViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val termSetId: String = savedStateHandle.get(termSetPartialAddArg)!!
}