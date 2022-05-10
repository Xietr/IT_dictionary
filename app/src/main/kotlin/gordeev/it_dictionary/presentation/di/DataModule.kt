package gordeev.it_dictionary.presentation.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gordeev.it_dictionary.data.DictionaryDataSource
import gordeev.it_dictionary.data.DictionaryRepository
import gordeev.it_dictionary.data.firebase.FirebaseDictionaryDataSource
import gordeev.it_dictionary.data.firebase.FirebaseDictionaryRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DataModule {
    @Singleton
    @Binds
    internal abstract fun provideLogger(bind: FirebaseDictionaryDataSource): DictionaryDataSource

    @Singleton
    @Binds
    internal abstract fun provideAnalytics(bind: FirebaseDictionaryRepository): DictionaryRepository
}