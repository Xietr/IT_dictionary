package gordeev.it_dictionary.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gordeev.it_dictionary.data.data_sources.remote.DictionaryRemoteDataSource
import gordeev.it_dictionary.data.data_sources.remote.firebase.DictionaryFirebaseDataSource
import gordeev.it_dictionary.data.repositories.DictionaryRepository
import gordeev.it_dictionary.data.repositories.DictionaryRepositoryImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DataModule {
    @Singleton
    @Binds
    internal abstract fun provideFirebaseDataSource(bind: DictionaryFirebaseDataSource): DictionaryRemoteDataSource

    @Singleton
    @Binds
    internal abstract fun provideDictionaryRepository(bind: DictionaryRepositoryImpl): DictionaryRepository
}