package gordeev.it_dictionary.presentation.screens.term_set_partial_add

import gordeev.it_dictionary.data.data_sources.local.entities.result.TermSetWithTerms

data class TermSetPartialAddViewState(
    val isSuccess: Boolean = false,
    val termSetWithTerms: TermSetWithTerms? = null
)