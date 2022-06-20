package gordeev.it_dictionary.presentation.screens.term_search

data class TermSearchViewState(
    val searchValue: String = "",
    val isSecretActivated: Boolean = false,
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)