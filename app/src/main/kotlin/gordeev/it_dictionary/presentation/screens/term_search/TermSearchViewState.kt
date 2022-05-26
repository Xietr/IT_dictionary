package gordeev.it_dictionary.presentation.screens.term_search

data class TermSearchViewState(
    val searchValue: String = "",
    val isLoading: Boolean = false,
    val isError: Boolean = false
)