package gordeev.it_dictionary.data.di

import android.content.Context
import android.os.Debug
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gordeev.it_dictionary.data.local.DictionaryRoomDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomDatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): DictionaryRoomDatabase {
        val builder = Room.databaseBuilder(context, DictionaryRoomDatabase::class.java, "dictionary.db")
        if (Debug.isDebuggerConnected()) {
            builder.allowMainThreadQueries()
        }
        return builder.build()
    }

    @Provides
    fun provideDictionaryDao(db: DictionaryRoomDatabase) = db.dictionaryDao()
}