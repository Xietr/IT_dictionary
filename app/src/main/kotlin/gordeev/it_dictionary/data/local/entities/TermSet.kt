package gordeev.it_dictionary.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import gordeev.it_dictionary.data.local.converters.TermsConverter
import gordeev.it_dictionary.data.local.entities.Term.Companion.fromRemoteTerm
import gordeev.it_dictionary.data.remote.entities.RemoteTermSet

@Entity(tableName = "dictionary")
data class TermSet(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    @TypeConverters(TermsConverter::class)
    val terms: List<Term>,
    val isFavorite: Boolean,
    override val pageStartKey: String?
) : PaginatedEntity {
    companion object {
        fun RemoteTermSet.fromRemoteTermSet(isFavorite: Boolean = false, pageStartKey: String? = null) =
            TermSet(id, name, description, terms.map { it.fromRemoteTerm(isFavorite) }, isFavorite, pageStartKey)
    }
}