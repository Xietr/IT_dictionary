package gordeev.it_dictionary.presentation.screens.favorite

import gordeev.it_dictionary.data.data_sources.local.entities.result.TermSetWithTerms
import gordeev.it_dictionary.presentation.screens.favorite.FavoriteScreenTab.TO_LEARN

data class FavoriteScreenState(
    val tab: FavoriteScreenTab = TO_LEARN,
    val termSetsWithTerms: List<TermSetWithTerms> = emptyList()
)

enum class FavoriteScreenTab {
    TO_LEARN, LEARNED
}