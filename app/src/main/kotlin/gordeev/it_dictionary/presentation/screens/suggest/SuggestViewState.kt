package gordeev.it_dictionary.presentation.screens.suggest

data class SuggestViewState(
    val isLoading: Boolean = false,
    val errorType: ErrorType? = null,
    val isSuccess: Boolean = false,
    val termName: String = "",
    val termSetName: String = "",
    val termSetsOptions: List<String> = emptyList(),
    val termMeaning: String = ""
)

enum class ErrorType {
    NETWORK,
    EMPTY_INPUT
}