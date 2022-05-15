package gordeev.it_dictionary.data

import androidx.paging.PagingSource
import gordeev.it_dictionary.data.remote.entities.RemoteTermSet

abstract class DictionaryPagingDataSource : PagingSource<String, RemoteTermSet>()