package gordeev.it_dictionary.data.data_sources.remote

import gordeev.it_dictionary.data.data_sources.remote.entities.RemoteTermSet

interface DictionaryRemoteDataSource {

    suspend fun getDictionaryPart(startKey: String?, limit: Int): List<RemoteTermSet>
}