package gordeev.it_dictionary.data.data_sources.local.entities.result

import androidx.room.Entity
import androidx.room.PrimaryKey
import gordeev.it_dictionary.R
import gordeev.it_dictionary.data.data_sources.remote.entities.responses.RemoteTermSet

@Entity
data class TermSet(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val backgroundUrl: Int
) {
    companion object {
        fun RemoteTermSet.fromRemoteTermSet() = TermSet(
            id, name, description, when {
                backgroundUrl.contains("1") -> R.drawable.first
                backgroundUrl.contains("2") -> R.drawable.second
                backgroundUrl.contains("3") -> R.drawable.third
                backgroundUrl.contains("4") -> R.drawable.fourth
                backgroundUrl.contains("5") -> R.drawable.fifth
                else -> R.drawable.special
            }
        )
    }
}