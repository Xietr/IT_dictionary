package gordeev.it_dictionary.data.data_sources.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gordeev.it_dictionary.data.data_sources.local.entities.Dictionary
import gordeev.it_dictionary.data.data_sources.local.entities.TermSet

@Dao
interface DictionaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: TermSet): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: Dictionary)

    @Query("SELECT * FROM dictionary LIMIT :loadSize")
    suspend fun getDictionaryPart(loadSize: Int): Dictionary

    @Query("SELECT * FROM dictionary WHERE id > :startId LIMIT :loadSize")
    suspend fun getDictionaryPart(startId: String, loadSize: Int): Dictionary

    @Query("SELECT * FROM dictionary WHERE id = :id")
    suspend fun getTermSet(id: String): TermSet?
}