package gordeev.it_dictionary.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import gordeev.it_dictionary.data.local.converters.TermsConverter
import gordeev.it_dictionary.data.local.daos.DictionaryDao
import gordeev.it_dictionary.data.local.entities.TermSet

@Database(entities = [TermSet::class], version = 1)
@TypeConverters(TermsConverter::class)
abstract class DictionaryRoomDatabase : RoomDatabase() {
    abstract fun dictionaryDao(): DictionaryDao
}