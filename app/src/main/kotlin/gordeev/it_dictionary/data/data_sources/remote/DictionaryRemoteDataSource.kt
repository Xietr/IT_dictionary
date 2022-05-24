package gordeev.it_dictionary.data.data_sources.remote

import gordeev.it_dictionary.data.data_sources.invokeStatus.InvokeStatus
import gordeev.it_dictionary.data.data_sources.remote.entities.responses.RemoteTermSet
import gordeev.it_dictionary.data.data_sources.remote.entities.requests.RequestToAddTerm
import kotlinx.coroutines.flow.Flow

interface DictionaryRemoteDataSource {

    suspend fun getDictionaryPart(startKey: String?, limit: Int): List<RemoteTermSet>

    suspend fun getAllTermSetsName(): List<String>

    fun sendRequestToAddTerm(requestToAddTerm: RequestToAddTerm): Flow<InvokeStatus>
}