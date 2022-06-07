package gordeev.it_dictionary.data.data_sources.local.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import gordeev.it_dictionary.data.data_sources.local.entities.result.Term
import gordeev.it_dictionary.data.data_sources.local.entities.result.TermSet
import gordeev.it_dictionary.data.data_sources.local.entities.result.TermSetWithTerm
import gordeev.it_dictionary.data.data_sources.local.entities.result.TermSetWithTerms
import gordeev.it_dictionary.data.data_sources.local.entities.update.UpdateTermIsFavorite
import gordeev.it_dictionary.data.data_sources.local.entities.update.UpdateTermIsLearned
import gordeev.it_dictionary.data.data_sources.local.entities.update.UpdateTermWithRemoteData
import kotlinx.coroutines.flow.Flow

@Dao
interface DictionaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTermSet(termSet: TermSet)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTermSets(entities: List<TermSet>)

    @Insert(entity = Term::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTerm(updateTermWithRemoteData: UpdateTermWithRemoteData): Long

    @Update(entity = Term::class)
    suspend fun updateTerm(updateTermWithRemoteData: UpdateTermWithRemoteData)

    @Transaction
    suspend fun insertOrUpdateTerm(updateTermWithRemoteData: UpdateTermWithRemoteData) {
        val id = insertTerm(updateTermWithRemoteData)
        if (id == -1L) {
            updateTerm(updateTermWithRemoteData)
        }
    }

    @Transaction
    @Query("SELECT * FROM termSet")
    fun getDictionaryPagingSource(): PagingSource<Int, TermSetWithTerms>

    @Transaction
    @Query("SELECT * FROM termSet JOIN term ON term.termSetId = termSet.id WHERE term.termName LIKE '%' || :termName || '%'")
    fun getDictionaryPagingSourceByTerm(termName: String): PagingSource<Int, TermSetWithTerm>

    @Transaction
    @Query("SELECT * FROM termSet WHERE id = :id")
    fun getTermSetObservableById(id: String): Flow<TermSetWithTerms?>

    @Update(entity = Term::class)
    suspend fun updateTermIsFavorite(updateTermIsFavorite: UpdateTermIsFavorite)

    @Update(entity = Term::class)
    suspend fun updateTermsAreFavorite(updateTermsAreFavorite: List<UpdateTermIsFavorite>)

    @Update(entity = Term::class)
    suspend fun updateTermIsLearned(updateTermIsLearned: UpdateTermIsLearned)

    @Query("SELECT * FROM term WHERE isFavorite")
    suspend fun getFavoriteTerms(): List<Term>

    @Query("SELECT * FROM termSet WHERE id = :id")
    suspend fun getTermSetById(id: String): TermSet
}