package gordeev.it_dictionary.data.data_sources.remote.firebase

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import gordeev.it_dictionary.data.data_sources.remote.DictionaryRemoteDataSource
import gordeev.it_dictionary.data.data_sources.remote.entities.RemoteTermSet
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

    companion object {
        private const val DICTIONARY_PATH = "dictionary"
    }
}