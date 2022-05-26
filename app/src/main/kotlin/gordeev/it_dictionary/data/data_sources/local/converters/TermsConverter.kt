package gordeev.it_dictionary.data.data_sources.local.converters

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import gordeev.it_dictionary.data.data_sources.local.entities.result.Term

class TermsConverter {
    private val moshi = Moshi.Builder().build()
    private val listOfTermsType = Types.newParameterizedType(List::class.java, Term::class.java)
    private val adapter = moshi.adapter<List<Term>>(listOfTermsType)

    @TypeConverter
    fun toString(terms: List<Term>): String =
        adapter.toJson(terms)

    @TypeConverter
    fun fromString(string: String): List<Term> =
        adapter.fromJson(string)!!
}