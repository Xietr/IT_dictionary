package gordeev.it_dictionary.presentation.screens.training

import dagger.hilt.android.scopes.ViewModelScoped
import gordeev.it_dictionary.data.data_sources.local.entities.result.TermSetWithTerms
import javax.inject.Inject

@ViewModelScoped
class CurrentTrainingTermSetWithTerms @Inject constructor() {
    lateinit var termSetWithTerms: TermSetWithTerms
}