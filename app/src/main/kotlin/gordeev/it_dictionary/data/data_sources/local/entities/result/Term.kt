package gordeev.it_dictionary.data.data_sources.local.entities.result

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity
data class Term(
    @PrimaryKey val termId: String,
    val termSetId: String,
    val termName: String,
    val meaning: String,
    @ColumnInfo(defaultValue = "false") val isFavorite: Boolean = false
)