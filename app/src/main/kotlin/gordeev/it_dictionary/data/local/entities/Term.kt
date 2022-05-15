package gordeev.it_dictionary.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import gordeev.it_dictionary.data.remote.entities.RemoteTerm

@JsonClass(generateAdapter = true)
@Entity
data class Term(
    @PrimaryKey val id: String,
    val name: String,
    val meaning: String,
    val isFavorite: Boolean
) {
    companion object {
        fun RemoteTerm.fromRemoteTerm(isFavorite: Boolean) = Term(id, name, meaning, isFavorite)
    }
}