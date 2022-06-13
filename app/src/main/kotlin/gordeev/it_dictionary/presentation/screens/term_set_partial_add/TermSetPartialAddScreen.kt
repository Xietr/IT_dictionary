package gordeev.it_dictionary.presentation.screens.term_set_partial_add

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import gordeev.it_dictionary.R
import gordeev.it_dictionary.data.data_sources.local.entities.result.Term
import gordeev.it_dictionary.data.data_sources.local.entities.result.TermSetWithTerms
import gordeev.it_dictionary.presentation.theme.checkboxColors
import gordeev.it_dictionary.presentation.ui.TextButton
import gordeev.it_dictionary.presentation.ui.TextButtonColors.Primary
import gordeev.it_dictionary.presentation.utils.drawUnderline
import gordeev.it_dictionary.presentation.utils.rememberStateWithLifecycle
import gordeev.it_dictionary.presentation.utils.screenEdgeOffsetHorizontal
import gordeev.it_dictionary.presentation.utils.screenEdgeOffsetVertical

@Composable
fun TermSetPartialAddScreen() {
    TermSetPartialAddScreen(hiltViewModel())
}

@Composable
private fun TermSetPartialAddScreen(
    viewModel: TermSetPartialAddViewModel
) {
    val termSet by rememberStateWithLifecycle(viewModel.termSet)
    termSet?.let {
        TermSetPartialAddScreen(
            it,
            onButtonClick = viewModel::onAddWordsClicked,
            onFavoriteChanged = viewModel::onFavoriteChanged
        )
    }
}

@Composable
private fun TermSetPartialAddScreen(
    termSetWithTerms: TermSetWithTerms,
    onButtonClick: () -> Unit,
    onFavoriteChanged: (Term, Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            with(termSetWithTerms.termSet) {
                Toolbar(name, description)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = screenEdgeOffsetHorizontal)
                .fillMaxSize(),
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                termSetWithTerms.terms.forEach { term ->
                    item {
                        AddTermSearchResult(term) {
                            onFavoriteChanged(term, it)
                        }
                    }
                }
            }
            TextButton(
                onClick = onButtonClick,
                modifier = Modifier
                    .padding(bottom = screenEdgeOffsetVertical)
                    .fillMaxWidth(),
                colors = Primary,
                textRes = R.string.add_terms_from_set_button
            )
        }
    }
}

@Composable
private fun Toolbar(name: String, description: String) {
    val onBackPressed = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = screenEdgeOffsetHorizontal)
    ) {
        Row {
            IconButton(onClick = { onBackPressed?.onBackPressed() }) {
                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
            }
            Text(
                text = name,
                style = MaterialTheme.typography.h4
            )
        }
        Text(
            modifier = Modifier.padding(screenEdgeOffsetHorizontal),
            text = description,
            style = MaterialTheme.typography.h6,
        )
    }
}

@Composable
private fun AddTermSearchResult(term: Term, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .drawUnderline()
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = term.isFavorite,
            onCheckedChange = onCheckedChange,
            colors = checkboxColors
        )
        Text(text = term.termName, style = MaterialTheme.typography.h6)
    }
}
