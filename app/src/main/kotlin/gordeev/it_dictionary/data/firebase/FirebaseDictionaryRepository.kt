package gordeev.it_dictionary.data.firebase

import gordeev.it_dictionary.data.DictionaryDataSource
import gordeev.it_dictionary.data.DictionaryRepository
import gordeev.it_dictionary.model.Dictionary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FirebaseDictionaryRepository @Inject constructor(
    private val dictionaryDataSource: DictionaryDataSource
) : DictionaryRepository {

    override fun getDictionaryPart(startKey: String?): Flow<Dictionary> =
        flow {
            emit(dictionaryDataSource.getDictionaryPart(startKey))
        }.flowOn(Dispatchers.IO)

    override suspend fun toggleFavorite(termSetId: String) {
        TODO("Not yet implemented")
    }
}
