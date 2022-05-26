package gordeev.it_dictionary.data.data_sources.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import gordeev.it_dictionary.data.data_sources.local.converters.TermsConverter
import gordeev.it_dictionary.data.data_sources.local.daos.DictionaryDao
import gordeev.it_dictionary.data.data_sources.local.entities.result.Term
import gordeev.it_dictionary.data.data_sources.local.entities.result.TermSet

@Database(entities = [TermSet::class, Term::class], version = 1)
@TypeConverters(TermsConverter::class)
abstract class DictionaryRoomDatabase : RoomDatabase() {
    abstract fun dictionaryDao(): DictionaryDao
}