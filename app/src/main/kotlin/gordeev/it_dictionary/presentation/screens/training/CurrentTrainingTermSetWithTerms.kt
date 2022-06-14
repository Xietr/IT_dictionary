package gordeev.it_dictionary.presentation.screens.training

import gordeev.it_dictionary.data.data_sources.local.entities.result.TermSetWithTerms
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentTrainingTermSetWithTerms @Inject constructor() {
    lateinit var termSetWithTerms: TermSetWithTerms
}