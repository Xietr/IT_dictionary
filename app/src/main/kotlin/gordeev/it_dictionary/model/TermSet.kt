package gordeev.it_dictionary.model

data class TermSet(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val terms: List<Term> = emptyList(),
    val isFavorite: Boolean = false
)