package gordeev.it_dictionary.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gordeev.it_dictionary.data.local.entities.Dictionary
import gordeev.it_dictionary.data.local.entities.TermSet
import kotlinx.coroutines.flow.Flow

@Dao
interface DictionaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: TermSet): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: Dictionary)

    @Query("SELECT * FROM dictionary WHERE pageStartKey = :pageStartKey")
    fun dictionaryObservable(pageStartKey: String): Flow<Dictionary>
}