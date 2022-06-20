package gordeev.it_dictionary.data.data_sources.remote.entities.responses

data class RemoteTermSet(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val terms: List<RemoteTerm> = emptyList(),
    val backgroundUrl: String = ""
)