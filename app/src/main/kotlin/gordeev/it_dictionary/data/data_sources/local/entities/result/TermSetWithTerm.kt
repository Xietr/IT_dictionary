package gordeev.it_dictionary.data.data_sources.local.entities.result

import androidx.room.Embedded

data class TermSetWithTerm(
    @Embedded
    val termSet: TermSet,
    @Embedded
    val term: Term
)