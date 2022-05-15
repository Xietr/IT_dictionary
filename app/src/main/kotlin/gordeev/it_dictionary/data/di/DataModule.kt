package gordeev.it_dictionary.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gordeev.it_dictionary.data.DictionaryPagingDataSource
import gordeev.it_dictionary.data.DictionaryRepository
import gordeev.it_dictionary.data.DictionaryRepositoryImpl
import gordeev.it_dictionary.data.remote.firebase.FirebaseDictionaryPagingDataSource
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DataModule {
    @Singleton
    @Binds
    internal abstract fun provideFirebaseDataSource(bind: FirebaseDictionaryPagingDataSource): DictionaryPagingDataSource

    @Singleton
    @Binds
    internal abstract fun provideAnalytics(bind: DictionaryRepositoryImpl): DictionaryRepository
}