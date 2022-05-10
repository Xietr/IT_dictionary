package gordeev.it_dictionary.presentation.screens.home

import androidx.compose.runtime.Immutable
import gordeev.it_dictionary.model.Dictionary

@Immutable
data class HomeViewState(
    val dictionary: Dictionary = emptyList(),
    val loading: Boolean = false
) {
    companion object {
        val Empty = HomeViewState()
    }
}