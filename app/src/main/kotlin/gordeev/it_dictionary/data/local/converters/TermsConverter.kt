package gordeev.it_dictionary.data.local.converters

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import gordeev.it_dictionary.data.local.entities.Term

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