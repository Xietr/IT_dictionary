package gordeev.it_dictionary.data.data_sources.remote.firebase

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import gordeev.it_dictionary.data.data_sources.invokeStatus.InvokeError
import gordeev.it_dictionary.data.data_sources.invokeStatus.InvokeStarted
import gordeev.it_dictionary.data.data_sources.invokeStatus.InvokeSuccess
import gordeev.it_dictionary.data.data_sources.remote.DictionaryRemoteDataSource
import gordeev.it_dictionary.data.data_sources.remote.entities.requests.RequestToAddTerm
import gordeev.it_dictionary.data.data_sources.remote.entities.responses.RemoteTermSet
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DictionaryFirebaseDataSource @Inject constructor() : DictionaryRemoteDataSource {

    private val database = Firebase.database

    override suspend fun getDictionaryPart(startKey: String?, limit: Int): List<RemoteTermSet> =
        database
            .getReference(DICTIONARY_PATH)
            .orderByKey()
            .run {
                startKey?.let {
                    startAfter(startKey)
                } ?: this
            }
            .limitToFirst(limit)
            .get()
            .await()
            .children
            .mapNotNull {
                it.getValue(RemoteTermSet::class.java)?.copy(id = it?.key ?: "")
            }

    override suspend fun getAllTermSetsName(): List<String> =
        database
            .getReference(DICTIONARY_PATH)
            .get()
            .await()
            .children
            .mapNotNull { it.child("name").getValue(String::class.java) }

    override fun sendRequestToAddTerm(requestToAddTerm: RequestToAddTerm) = callbackFlow {
        trySend(InvokeStarted)
        database
            .getReference(REQUESTS_PATH)
            .push()
            .setValue(requestToAddTerm)
            .addOnSuccessListener {
                trySend(InvokeSuccess)
                close()
            }
            .addOnFailureListener {
                trySend(InvokeError(it))
                close(it)
            }
        awaitClose()
    }

    companion object {
        private const val DICTIONARY_PATH = "dictionary"
        private const val REQUESTS_PATH = "requests"
    }
}