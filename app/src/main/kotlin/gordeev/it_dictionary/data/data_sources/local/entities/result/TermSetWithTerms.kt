package gordeev.it_dictionary.data.data_sources.local.entities.result

import androidx.room.Embedded
import androidx.room.Relation

data class TermSetWithTerms(
    @Embedded
    val termSet: TermSet,
    @Relation(parentColumn = "id", entityColumn = "termSetId")
    val terms: List<Term>
)