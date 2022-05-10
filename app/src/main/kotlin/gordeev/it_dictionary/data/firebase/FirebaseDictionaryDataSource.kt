package gordeev.it_dictionary.data.firebase

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import gordeev.it_dictionary.data.DictionaryDataSource
import gordeev.it_dictionary.model.Dictionary
import gordeev.it_dictionary.model.TermSet
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseDictionaryDataSource @Inject constructor() : DictionaryDataSource {

    private val database = Firebase.database

    override suspend fun getDictionaryPart(startKey: String?): Dictionary =
        database
            .getReference(DICTIONARY_PATH)
            .orderByKey()
            .run {
                startKey?.let {
                    startAt(it)
                } ?: this
            }
            .limitToFirst(2)
            .get()
            .await()
            .children
            .mapNotNull {
                it.getValue(TermSet::class.java)?.copy(id = it?.key ?: "")
            }

    companion object {
        private const val DICTIONARY_PATH = "dictionary"
        private const val LENGTH = 10
    }
}