@file:OptIn(ExperimentalPagingApi::class)

package gordeev.it_dictionary.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import gordeev.it_dictionary.data.data_sources.local.daos.DictionaryDao
import gordeev.it_dictionary.data.data_sources.local.entities.result.TermSetWithTerm
import gordeev.it_dictionary.data.data_sources.local.entities.result.TermSetWithTerms
import gordeev.it_dictionary.data.data_sources.local.entities.update.UpdateTermIsFavorite
import gordeev.it_dictionary.data.data_sources.local.entities.update.UpdateTermIsLearned
import gordeev.it_dictionary.data.data_sources.remote.DictionaryRemoteDataSource
import gordeev.it_dictionary.data.data_sources.remote.entities.requests.RequestToAddTerm
import gordeev.it_dictionary.data.data_sources.utils.InvokeStatus
import gordeev.it_dictionary.data.utils.DictionaryRemoteMediator
import gordeev.it_dictionary.data.utils.DictionarySearchRemoteMediator
import kotlinx.coroutines.CoroutineStart.LAZY
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DictionaryRepositoryImpl @Inject constructor(
    private val dictionaryRemoteDataSource: DictionaryRemoteDataSource,
    private val dictionaryDao: DictionaryDao
) : DictionaryRepository {

    override fun termSetWithTermsPagingSource(pagingConfig: PagingConfig): Flow<PagingData<TermSetWithTerms>> =
        Pager(
            config = pagingConfig,
            remoteMediator = DictionaryRemoteMediator(dictionaryDao, dictionaryRemoteDataSource),
            pagingSourceFactory = { dictionaryDao.getDictionaryPagingSource() }
        ).flow.flowOn(Dispatchers.IO)

    override fun sendRequestToAddTerm(
        name: String,
        meaning: String,
        termSetName: String
    ): Flow<InvokeStatus> =
        dictionaryRemoteDataSource.sendRequestToAddTerm(
            RequestToAddTerm(
                name,
                meaning,
                termSetName
            )
        )

    @OptIn(DelicateCoroutinesApi::class)
    val allTermSets = GlobalScope.async(
        Dispatchers.IO,
        start = LAZY
    ) { dictionaryRemoteDataSource.getAllTermSetsName() }

    override fun observableTermSetsByName(name: String): Flow<List<String>> =
        flow {
            emit(
                allTermSets.await().filter { it.contains(name) }
            )
        }

    override fun observableTermSetById(id: String): Flow<TermSetWithTerms> =
        dictionaryDao.getTermSetObservableById(id)

    override suspend fun setTermIsFavorite(termId: String, isFavorite: Boolean) {
        dictionaryDao.updateTermIsFavorite(UpdateTermIsFavorite(termId, isFavorite))
    }

    override suspend fun setTermsAreFavorite(termIdToIsFavorite: Map<String, Boolean>) {
        dictionaryDao.updateTermsAreFavorite(termIdToIsFavorite.map {
            UpdateTermIsFavorite(
                it.key,
                it.value
            )
        })
    }

    override suspend fun setTermIsLearned(termId: String, isLearned: Boolean) {
        dictionaryDao.updateTermIsLearned(UpdateTermIsLearned(termId, isLearned))
    }

    @Throws(Exception::class)
    override fun termSetWithTermsPagingSourceByTermName(
        pagingConfig: PagingConfig,
        termNameQuery: String
    ): Flow<PagingData<TermSetWithTerm>> =
        Pager(
            config = pagingConfig,
            remoteMediator = DictionarySearchRemoteMediator(
                termNameQuery,
                dictionaryDao,
                dictionaryRemoteDataSource
            ),
            pagingSourceFactory = { dictionaryDao.getDictionaryPagingSourceByTerm(termNameQuery) }
        ).flow.flowOn(Dispatchers.IO)

    override fun getAllFavoriteTermSetsWithTerms(): Flow<List<TermSetWithTerms>> = flow {
        val favoriteTermSetsIds = mutableSetOf<String>()
        val favoriteTerms = dictionaryDao.getFavoriteTerms().also { terms ->
            terms.map {
                favoriteTermSetsIds.add(it.termSetId)
            }
        }
        emit(
            favoriteTermSetsIds.map { favoriteTermSetId ->
                TermSetWithTerms(
                    termSet = dictionaryDao.getTermSetById(favoriteTermSetId),
                    terms = favoriteTerms.filter { it.termSetId == favoriteTermSetId }
                )
            }
        )
    }
}