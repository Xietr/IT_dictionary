package gordeev.it_dictionary.data.data_sources.local.entities.result

import androidx.room.Entity
import androidx.room.PrimaryKey
import gordeev.it_dictionary.data.data_sources.remote.entities.responses.RemoteTermSet

@Entity
data class TermSet(
    @PrimaryKey override val id: String,
    val name: String,
    val description: String,
) : PaginatedEntity {
    companion object {
        fun RemoteTermSet.fromRemoteTermSet() = TermSet(id, name, description)
    }
}