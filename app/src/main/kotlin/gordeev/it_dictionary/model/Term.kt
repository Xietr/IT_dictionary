package gordeev.it_dictionary.model

data class Term(
    val id: String = "",
    val name: String = "",
    val meaning: String = "",
    val isFavorite: Boolean = false
)