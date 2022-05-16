package gordeev.it_dictionary.data.di

import androidx.paging.PagingSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gordeev.it_dictionary.data.DictionaryRepositoryImpl
import gordeev.it_dictionary.data.data_sources.local.entities.TermSet
import gordeev.it_dictionary.data.data_sources.remote.DictionaryRemoteDataSource
import gordeev.it_dictionary.data.data_sources.remote.firebase.DictionaryFirebaseDataSource
import gordeev.it_dictionary.data.repositories.DictionaryRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DataModule {
    @Singleton
    @Binds
    internal abstract fun provideFirebaseDataSource(bind: DictionaryFirebaseDataSource): DictionaryRemoteDataSource

    @Binds
    internal abstract fun provideDictionaryPagingSource(bind: DictionaryRepository): PagingSource<String, TermSet>

    @Singleton
    @Binds
    internal abstract fun provideDictionaryRepository(bind: DictionaryRepositoryImpl): gordeev.it_dictionary.data.DictionaryRepository
}