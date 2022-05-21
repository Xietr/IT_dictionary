package gordeev.it_dictionary.data.repositories

import gordeev.it_dictionary.data.data_sources.local.entities.TermSet
import kotlinx.coroutines.flow.Flow

interface TermSetRepository {

    fun getTermSetObservable(termSetId: String): Flow<TermSet?>
}