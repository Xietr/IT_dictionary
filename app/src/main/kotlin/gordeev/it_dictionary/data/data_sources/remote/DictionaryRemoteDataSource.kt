package gordeev.it_dictionary.data.data_sources.remote

import gordeev.it_dictionary.data.data_sources.remote.entities.requests.RequestToAddTerm
import gordeev.it_dictionary.data.data_sources.remote.entities.responses.RemoteTermSet
import gordeev.it_dictionary.data.data_sources.utils.InvokeStatus
import kotlinx.coroutines.flow.Flow

interface DictionaryRemoteDataSource {

    @Throws(Exception::class)
    suspend fun getDictionaryPart(startKey: String? = null, limit: Int): List<RemoteTermSet>

    @Throws(Exception::class)
    suspend fun getAllTermSetsName(): List<String>

    fun sendRequestToAddTerm(requestToAddTerm: RequestToAddTerm): Flow<InvokeStatus>
}