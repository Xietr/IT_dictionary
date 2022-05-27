package gordeev.it_dictionary.presentation.screens.term_search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import gordeev.it_dictionary.R.string
import gordeev.it_dictionary.data.data_sources.local.entities.result.TermSetWithTerm
import gordeev.it_dictionary.presentation.theme.Text20
import gordeev.it_dictionary.presentation.theme.checkboxColors
import gordeev.it_dictionary.presentation.ui.SearchTextField
import gordeev.it_dictionary.presentation.ui.TextButton
import gordeev.it_dictionary.presentation.ui.TextButtonColors.Primary
import gordeev.it_dictionary.presentation.utils.drawUnderline
import gordeev.it_dictionary.presentation.utils.rememberFlowWithLifecycle
import gordeev.it_dictionary.presentation.utils.rememberStateWithLifecycle
import gordeev.it_dictionary.presentation.utils.screenEdgeOffsetHorizontal
import gordeev.it_dictionary.presentation.utils.screenEdgeOffsetVertical

@Composable
fun TermSearchScreen() {
    TermSearchScreen(viewModel = hiltViewModel())
}

@Composable
private fun TermSearchScreen(
    viewModel: TermSearchViewModel
) {
    val state = rememberStateWithLifecycle(stateFlow = viewModel.state).value
    val pagingItems = rememberFlowWithLifecycle(viewModel.pagingTermSetWithTermFlow).collectAsLazyPagingItems()
    TermSearchScreen(
        state,
        pagingItems,
        { viewModel.search(it) },
        { termSetWithTerm, isFavorite -> viewModel.onFavoriteChanged(termSetWithTerm, isFavorite) },
        { viewModel.onAddWordsClicked() }
    )
}

@Composable
private fun TermSearchScreen(
    viewState: TermSearchViewState,
    list: LazyPagingItems<TermSetWithTerm>,
    onSearchChanged: (String) -> Unit,
    onFavoriteChanged: (TermSetWithTerm, Boolean) -> Unit,
    onButtonClick: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = screenEdgeOffsetHorizontal, top = screenEdgeOffsetVertical, end = screenEdgeOffsetHorizontal)
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            SearchTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .focusRequester(focusRequester),
                value = viewState.searchValue,
                onValueChange = onSearchChanged
            )
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                for (index in 0 until list.itemCount) {
                    list[index]?.let { termSetWithTerm ->
                        item {
                            AddTermSearchResult(
                                termSetWithTerm
                            ) {
                                onFavoriteChanged(termSetWithTerm, it)
                            }
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
                textRes = string.add_terms_from_set_button
            )
        }
    }
}

@Composable
private fun AddTermSearchResult(
    termSetWithTerm: TermSetWithTerm,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .drawUnderline()
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = termSetWithTerm.term.isFavorite, onCheckedChange = onCheckedChange, colors = checkboxColors)
        Column {
            Text(text = termSetWithTerm.term.termName, style = MaterialTheme.typography.button)
            Text(text = termSetWithTerm.termSet.name, style = MaterialTheme.typography.subtitle1, color = Text20)
        }
    }
}
