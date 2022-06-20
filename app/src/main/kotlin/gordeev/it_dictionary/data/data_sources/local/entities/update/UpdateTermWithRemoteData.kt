package gordeev.it_dictionary.data.data_sources.local.entities.update

import gordeev.it_dictionary.data.data_sources.remote.entities.responses.RemoteTerm

class UpdateTermWithRemoteData(
    val termId: String,
    val termSetId: String,
    val termName: String,
    val meaning: String,
) {
    companion object {
        fun RemoteTerm.fromRemoteTerm(termSetId: String) =
            UpdateTermWithRemoteData(id, termSetId, name, meaning)
    }
}