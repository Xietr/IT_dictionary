package gordeev.it_dictionary.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import gordeev.it_dictionary.data.DictionaryRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    dictionaryRepository: DictionaryRepository
) : ViewModel() {

    val pagedList = dictionaryRepository.getDictionaryPart(PAGING_CONFIG).cachedIn(viewModelScope)

    fun addTermSetToFavorite(id: String) {
    }

    companion object {
        private val PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            initialLoadSize = 14,
        )
    }
}