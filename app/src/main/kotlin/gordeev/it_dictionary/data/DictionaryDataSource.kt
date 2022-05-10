package gordeev.it_dictionary.data

import gordeev.it_dictionary.model.Dictionary

interface DictionaryDataSource {

    suspend fun getDictionaryPart(startKey: String?): Dictionary
}