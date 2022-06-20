package gordeev.it_dictionary.presentation.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import gordeev.it_dictionary.R
import gordeev.it_dictionary.data.data_sources.local.entities.result.TermSetWithTerms
import gordeev.it_dictionary.presentation.ui.SearchTextField
import gordeev.it_dictionary.presentation.utils.*

@Composable
fun HomeScreen(
    openPartialAddFromSet: (termSetId: String) -> Unit,
    openSearchScreen: () -> Unit
) {
    HomeScreen(hiltViewModel(), openPartialAddFromSet, openSearchScreen)
}

@Composable
private fun HomeScreen(
    viewModel: HomeViewModel,
    openPartialAddFromSet: (termSetId: String) -> Unit = {},
    openSearchScreen: () -> Unit
) {
    val pagingItems = rememberFlowWithLifecycle(viewModel.pagedList).collectAsLazyPagingItems()
    val selectedTermSetId by rememberStateWithLifecycle(viewModel.selectedTermSetId)

    HomeScreen(
        list = pagingItems,
        isLoading = pagingItems.loadState.run { refresh == LoadState.Loading || append == LoadState.Loading },
        dialogVisible = selectedTermSetId != null,
        onRefresh = { pagingItems.refresh() },
        onDismissRequest = { viewModel.onCloseDialog() },
        onChooseWordsClicked = { openPartialAddFromSet(selectedTermSetId!!) },
        onChooseAllWordsClicked = { viewModel.addTermSetToFavorite() },
        onFavoriteClick = { viewModel.onOpenDialog(it) },
        onTermSetClick = { openPartialAddFromSet(it.termSet.id) },
        onSearchClicked = openSearchScreen
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeScreen(
    isLoading: Boolean,
    list: LazyPagingItems<TermSetWithTerms>,
    dialogVisible: Boolean,
    onRefresh: () -> Unit,
    onDismissRequest: () -> Unit,
    onChooseWordsClicked: () -> Unit,
    onChooseAllWordsClicked: () -> Unit,
    onFavoriteClick: (TermSetWithTerms) -> Unit,
    onTermSetClick: (TermSetWithTerms) -> Unit,
    onSearchClicked: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = screenEdgeOffsetHorizontal,
                top = screenEdgeOffsetVertical,
                end = screenEdgeOffsetHorizontal
            )
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
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSearchClicked() },
                readOnly = true,
                enabled = false
            )
            Text(
                text = stringResource(id = R.string.home_screen_title),
                modifier = Modifier.padding(top = 32.dp, bottom = 20.dp),
                style = MaterialTheme.typography.h5
            )
            SwipeRefresh(
                state = rememberSwipeRefreshState(isLoading),
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
                    for (index in 0 until list.itemCount) {
                        list[index]?.let {
                            if (it.termSet.id.contains("secret").not()) {
                                item {
                                    HomeItem(
                                        termSetWithTerms = it,
                                        onClick = { onTermSetClick(it) },
                                        onFavoriteClick = { onFavoriteClick(it) },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeItem(
    termSetWithTerms: TermSetWithTerms,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.04f)
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = termSetWithTerms.termSet.backgroundUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            CompositionLocalProvider(LocalContentColor provides Color.White) {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = termSetWithTerms.termSet.name,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 16.dp),
                    )
                    IconButton(
                        onClick = onFavoriteClick,
                        modifier = Modifier.padding(top = 4.dp, end = 4.dp)
                    ) {
                        Icon(
                            painter = if (termSetWithTerms.terms.any { it.isFavorite }) {
                                painterResource(id = R.drawable.favorite_filled)
                            } else painterResource(
                                id = R.drawable.favorite_outlined
                            ),
                            contentDescription = null,
                        )
                    }
                }
                Text(
                    stringQuantityResource(R.plurals.terms_amount, termSetWithTerms.terms.count()),
                    style = MaterialTheme.typography.subtitle1,
                )
            }
        }
    }
}