package gordeev.it_dictionary.data.repositories

import gordeev.it_dictionary.data.data_sources.local.daos.DictionaryDao
import gordeev.it_dictionary.data.data_sources.local.entities.TermSet
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TermSetRepositoryImp @Inject constructor(
    private val dictionaryDao: DictionaryDao
) : TermSetRepository {

    override fun getTermSetObservable(termSetId: String): Flow<TermSet?> = dictionaryDao.getTermSetObservable(termSetId)


}