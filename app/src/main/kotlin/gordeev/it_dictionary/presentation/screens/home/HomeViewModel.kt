package gordeev.it_dictionary.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gordeev.it_dictionary.data.DictionaryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    dictionaryRepository: DictionaryRepository
) : ViewModel() {

    var loading = false

    val state: StateFlow<HomeViewState> = combine(dictionaryRepository.getDictionaryPart(null), MutableStateFlow(loading)) { list, loading ->
        HomeViewState(list, loading)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeViewState.Empty,
    )

    init {
        viewModelScope.launch {

        }
    }

    fun onRefresh() {
    }

    fun addTermSetToFavorite(id: String) {
    }
}