package gordeev.it_dictionary.presentation.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import gordeev.it_dictionary.R
import gordeev.it_dictionary.model.TermSet
import gordeev.it_dictionary.presentation.ui.SearchTextField
import gordeev.it_dictionary.presentation.utils.rememberStateWithLifecycle
import gordeev.it_dictionary.presentation.utils.stringQuantityResource

@Composable
fun HomeScreen(
    openPartialAddFromSet: (termSetId: String) -> Unit,
) {
    HomeScreen(hiltViewModel(), openPartialAddFromSet)
}

@Composable
private fun HomeScreen(
    viewModel: HomeViewModel,
    onChooseWordsClicked: (termSetId: String) -> Unit = {},
) {
    val viewState by rememberStateWithLifecycle(stateFlow = viewModel.state)

    var termSetIdToAdd by remember { mutableStateOf<String?>(null) }
    val dialogVisible by remember(termSetIdToAdd) { mutableStateOf(termSetIdToAdd != null) }

    HomeScreen(
        state = viewState,
        dialogVisible = dialogVisible,
        onRefresh = { viewModel.onRefresh() },
        onDismissRequest = { termSetIdToAdd = null },
        onChooseWordsClicked = { onChooseWordsClicked(termSetIdToAdd!!) },
        onChooseAllWordsClicked = { viewModel.addTermSetToFavorite(termSetIdToAdd!!) },
        onFavoriteClicked = { termSetIdToAdd = it }
    )
}

@Preview
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeScreen(
    state: HomeViewState = HomeViewState.Empty,
    dialogVisible: Boolean = false,
    onRefresh: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
    onChooseWordsClicked: () -> Unit = {},
    onChooseAllWordsClicked: () -> Unit = {},
    onFavoriteClicked: (id: String) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        if (dialogVisible) {
            HomeScreenDialog(
                onDismissRequest,
                onChooseWordsClicked,
                onChooseAllWordsClicked
            )
        }
        Column(modifier = Modifier.padding(paddingValues)) {
            SearchTextField(
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {}
            )
            Text(
                text = stringResource(id = R.string.home_screen_title),
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
                style = MaterialTheme.typography.h5.copy(fontWeight = Bold)
            )
            SwipeRefresh(
                state = rememberSwipeRefreshState(state.loading),
                onRefresh = onRefresh,
                indicator = { state, trigger ->
                    SwipeRefreshIndicator(
                        state = state,
                        refreshTriggerDistance = trigger,
                        scale = true
                    )
                }
            ) {
                LazyVerticalGrid(
                    cells = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = paddingValues,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    state.dictionary.forEach {
                        item {
                            HomeItem(
                                it,
                                onFavoriteClicked
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeItem(
    termSet: TermSet = TermSet(),
    onFavoriteClicked: (id: String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .size(150.dp)
            .background(Companion.Cyan, RoundedCornerShape(24.dp))
            .padding(start = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Text(
                text = termSet.name,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 16.dp)
            )
            IconButton(
                onClick = { onFavoriteClicked(termSet.id) },
                modifier = Modifier.padding(top = 4.dp, end = 4.dp)
            ) {
                Icon(
                    imageVector = if (termSet.isFavorite) Icons.Default.Favorite else Outlined.Favorite,
                    contentDescription = null,
                )
            }
        }
        Text(
            stringQuantityResource(R.plurals.terms_amount, termSet.terms.count())
        )
    }
}