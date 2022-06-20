package gordeev.it_dictionary.presentation.screens.term_search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import gordeev.it_dictionary.R
import gordeev.it_dictionary.data.data_sources.local.entities.result.TermSetWithTerm
import gordeev.it_dictionary.presentation.theme.Text20
import gordeev.it_dictionary.presentation.theme.checkboxColors
import gordeev.it_dictionary.presentation.ui.SearchTextField
import gordeev.it_dictionary.presentation.ui.TextButton
import gordeev.it_dictionary.presentation.ui.TextButtonColors.Primary
import gordeev.it_dictionary.presentation.utils.*
import kotlinx.coroutines.launch

@Composable
fun TermSearchScreen() {
    TermSearchScreen(viewModel = hiltViewModel())
}

@Composable
private fun TermSearchScreen(
    viewModel: TermSearchViewModel
) {
    val state by rememberStateWithLifecycle(stateFlow = viewModel.state)
    val pagingItems =
        rememberFlowWithLifecycle(viewModel.pagingTermSetWithTermFlow).collectAsLazyPagingItems()
    TermSearchScreen(
        state,
        pagingItems,
        { viewModel.search(it) },
        { termSetWithTerm, isFavorite -> viewModel.onFavoriteChanged(termSetWithTerm, isFavorite) },
        { viewModel.onAddWordsClicked() },
        { viewModel.onSecretDialogHide() }
    )
}

@Composable
private fun TermSearchScreen(
    viewState: TermSearchViewState,
    list: LazyPagingItems<TermSetWithTerm>,
    onSearchChanged: (String) -> Unit,
    onFavoriteChanged: (TermSetWithTerm, Boolean) -> Unit,
    onButtonClick: () -> Unit,
    onSecretDialogDismiss: () -> Unit
) {
    val context = LocalContext.current
    val successMessage = context.getString(R.string.added_to_favorite_successfully)
    val secretActivatedMessage = context.getString(R.string.secret_activated_message)
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(viewState.isSuccess) {
        if (viewState.isSuccess) {
            coroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(successMessage)
            }
        }
    }
    if (viewState.isSecretActivated) {
        Dialog(
            onDismissRequest = { onSecretDialogDismiss() }
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colors.surface
            ) {
                Text(text = secretActivatedMessage, modifier = Modifier.padding(24.dp))
            }
        }
    }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = screenEdgeOffsetHorizontal,
                top = screenEdgeOffsetVertical,
                end = screenEdgeOffsetHorizontal
            ),
        scaffoldState = scaffoldState
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
                textRes = R.string.add_terms_from_set_button
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
            .padding(vertical = 16.dp)
            .clickable {
                onCheckedChange(termSetWithTerm.term.isFavorite.not())
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Checkbox(
            checked = termSetWithTerm.term.isFavorite,
            onCheckedChange = null,
            colors = checkboxColors
        )
        Column {
            Text(text = termSetWithTerm.term.termName, style = MaterialTheme.typography.button)
            Text(
                text = termSetWithTerm.termSet.name,
                style = MaterialTheme.typography.subtitle1,
                color = Text20
            )
        }
    }
}
