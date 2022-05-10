package gordeev.it_dictionary.data

import gordeev.it_dictionary.model.Dictionary
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {

    fun getDictionaryPart(startKey: String?): Flow<Dictionary>

    suspend fun toggleFavorite(termSetId: String)
}