package gordeev.it_dictionary.data.remote.entities

data class RemoteTermSet(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val terms: List<RemoteTerm> = emptyList()
)