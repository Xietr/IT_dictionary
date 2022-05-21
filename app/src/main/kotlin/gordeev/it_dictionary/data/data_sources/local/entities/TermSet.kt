package gordeev.it_dictionary.data.data_sources.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import gordeev.it_dictionary.data.data_sources.local.converters.TermsConverter
import gordeev.it_dictionary.data.data_sources.local.entities.Term.Companion.fromRemoteTerm
import gordeev.it_dictionary.data.data_sources.remote.entities.RemoteTermSet

@Entity(tableName = "dictionary")
data class TermSet(
    @PrimaryKey override val id: String,
    val name: String,
    val description: String,
    @TypeConverters(TermsConverter::class)
    val terms: List<Term>
) : PaginatedEntity {

    val isFavorite: Boolean
        get() = terms.any { it.isFavorite }

    companion object {
        fun RemoteTermSet.fromRemoteTermSet(isFavorite: Boolean = false) =
            TermSet(id, name, description, terms.map { it.fromRemoteTerm(isFavorite) })
    }
}