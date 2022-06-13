package gordeev.it_dictionary.presentation.screens.favorite

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells.Fixed
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import gordeev.it_dictionary.R.plurals
import gordeev.it_dictionary.R.string
import gordeev.it_dictionary.data.data_sources.local.entities.result.TermSetWithTerms
import gordeev.it_dictionary.presentation.screens.favorite.FavoriteScreenTab.LEARNED
import gordeev.it_dictionary.presentation.screens.favorite.FavoriteScreenTab.TO_LEARN
import gordeev.it_dictionary.presentation.utils.rememberStateWithLifecycle
import gordeev.it_dictionary.presentation.utils.screenEdgeOffsetHorizontal
import gordeev.it_dictionary.presentation.utils.stringQuantityResource

@Composable
fun FavoriteScreen(
    openTrainingScreen: () -> Unit,
) {
    FavoriteScreen(openTrainingScreen, hiltViewModel())
}

@Composable
private fun FavoriteScreen(
    openTrainingScreen: () -> Unit,
    viewModel: FavoriteViewModel
) {
    val state by rememberStateWithLifecycle(viewModel.state)
    FavoriteScreen(
        state = state,
        openTrainingScreen = {
            viewModel.onOpenTrainingScreen(it)
            openTrainingScreen()
        },
        onTabClicked = viewModel::changeTab,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FavoriteScreen(
    state: FavoriteScreenState,
    openTrainingScreen: (TermSetWithTerms) -> Unit,
    onTabClicked: (FavoriteScreenTab) -> Unit,
) {
    val gradientPairs = listOf(
        Color(0xFFCB5DFF) to Color(0xFF1D41BE), //purple
        Color(0xFFD55C05) to Color(0xFFFABF26), //orange
        Color(0xFFB0FF4B) to Color(0xFF11876B), //green
        Color(0xFF9C1EBC) to Color(0xFFFF7676), //pink
    )
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = screenEdgeOffsetHorizontal,
                top = 24.dp,
                end = screenEdgeOffsetHorizontal
            )
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Text(
                modifier = Modifier.padding(bottom = 20.dp),
                text = stringResource(string.favorite_screen_title),
                style = MaterialTheme.typography.h5
            )
            Row(modifier = Modifier.padding(bottom = 24.dp)) {
                val activeTabColor = MaterialTheme.colors.primary
                val unActiveTabColor = MaterialTheme.colors.secondary
                val firstTabColor = if (state.tab == TO_LEARN) activeTabColor else unActiveTabColor
                val secondTabColor = if (state.tab == LEARNED) activeTabColor else unActiveTabColor
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = firstTabColor,
                            shape = MaterialTheme.shapes.small.copy(
                                bottomEnd = ZeroCornerSize,
                                topEnd = ZeroCornerSize
                            ),
                        )
                        .clickable {
                            onTabClicked(TO_LEARN)
                        }
                        .padding(vertical = 8.dp),
                    text = stringResource(string.favorite_screen_to_learn),
                    color = MaterialTheme.colors.contentColorFor(firstTabColor),
                    style = MaterialTheme.typography.subtitle1,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = secondTabColor,
                            shape = MaterialTheme.shapes.small.copy(
                                topStart = ZeroCornerSize,
                                bottomStart = ZeroCornerSize
                            ),
                        )
                        .clickable {
                            onTabClicked(LEARNED)
                        }
                        .padding(vertical = 8.dp),
                    text = stringResource(string.favorite_screen_learned),
                    color = MaterialTheme.colors.contentColorFor(secondTabColor),
                    style = MaterialTheme.typography.subtitle1,
                    textAlign = TextAlign.Center
                )
            }
            LazyVerticalGrid(
                cells = Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = paddingValues,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                state.termSetsWithTerms.forEach {
                    item {
                        HomeItem(
                            modifier = Modifier,
                            termSetWithTerms = it,
                            onClick = { openTrainingScreen(it) },
                            gradientPairs[0]
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeItem(
    modifier: Modifier,
    termSetWithTerms: TermSetWithTerms,
    onClick: () -> Unit,
    colors: Pair<Color, Color>
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(
                Brush.linearGradient(
                    colors.toList()
                ), RoundedCornerShape(24.dp)
            )
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
                    onClick = onClick,
                    modifier = Modifier.padding(top = 4.dp, end = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                    )
                }
            }
            Text(
                stringQuantityResource(plurals.terms_amount, termSetWithTerms.terms.count()),
                style = MaterialTheme.typography.subtitle1,
            )
        }
    }
}