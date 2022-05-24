package gordeev.it_dictionary.presentation.screens.suggest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gordeev.it_dictionary.data.data_sources.invokeStatus.InvokeError
import gordeev.it_dictionary.data.data_sources.invokeStatus.InvokeStarted
import gordeev.it_dictionary.data.data_sources.invokeStatus.InvokeSuccess
import gordeev.it_dictionary.data.repositories.DictionaryRepository
import gordeev.it_dictionary.presentation.screens.suggest.ErrorType.EMPTY_INPUT
import gordeev.it_dictionary.presentation.screens.suggest.ErrorType.NETWORK
import gordeev.it_dictionary.presentation.utils.combine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuggestViewModel @Inject constructor(
    private val dictionaryRepository: DictionaryRepository
) : ViewModel() {

    private val loadingState = MutableStateFlow(false)
    private val successState = MutableStateFlow(false)
    private val errorState = MutableStateFlow<ErrorType?>(null)

    private val termName = MutableStateFlow("")
    private val termSetName = MutableStateFlow("")
    private val termMeaning = MutableStateFlow("")

    private var termSetNameOptions = MutableStateFlow(emptyList<String>())

    val state = combine(
        loadingState, successState, errorState,
        termName, termSetName, termSetNameOptions, termMeaning,
    ) { loading, success, error, termName, termSetName, termSetNameOptions, termMeaning ->
        SuggestViewState(
            isLoading = loading && success.not() && error == null,
            errorType = error,
            isSuccess = success,
            termName = termName,
            termSetName = termSetName,
            termSetsOptions = termSetNameOptions,
            termMeaning = termMeaning
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SuggestViewState()
    )

    init {
        viewModelScope.launch {
            termSetName.collect {
                dictionaryRepository.getTermSetsByName(it).collect(termSetNameOptions)
            }
        }
    }

    fun onBottomButtonClick() {
        viewModelScope.launch {
            if (termName.value.isNotBlank() && termMeaning.value.isNotBlank() && termSetName.value.isNotBlank()) {
                dictionaryRepository
                    .sendRequestToAddTerm(termName.value, termMeaning.value, termSetName.value)
                    .collect {
                        loadingState.emit(it is InvokeStarted)
                        successState.emit(it is InvokeSuccess)
                        errorState.emit(if (it is InvokeError) NETWORK else null)
                    }
            } else {
                errorState.value = EMPTY_INPUT
            }
        }
    }

    fun onTermNameChanged(value: String) {
        termName.value = value
    }

    fun onTermSetChanged(value: String) {
        termSetName.value = value
    }

    fun onTermMeaningChanged(value: String) {
        termMeaning.value = value
    }
}