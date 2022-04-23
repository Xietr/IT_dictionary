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
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import gordeev.it_dictionary.R
import gordeev.it_dictionary.presentation.ui.SearchTextField
import gordeev.it_dictionary.presentation.utils.stringQuantityResource

@Preview
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onRefresh: () -> Unit = {}
) {
    val scaffoldState = rememberScaffoldState()

    var state = object {
        val isLoading = false
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
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
                state = rememberSwipeRefreshState(state.isLoading),
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
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(12) {
                        HomeItem()
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeItem(

) {
    val item = object {
        val title: String = "title\ntitle\ntitle\ntitle"
        val isFavorite: Boolean = true
        val onFavoriteClicked: () -> Unit = { println("нажато типа") }
        val numberOfTerms = 0 //todo
    }

    Column(
        modifier = Modifier
            .size(150.dp)
            .background(Companion.Cyan, RoundedCornerShape(24.dp))
            .padding(start = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Text(
                text = item.title,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 16.dp)
            )
            IconButton(onClick = item.onFavoriteClicked, modifier = Modifier.padding(top = 4.dp, end = 4.dp)) {
                Icon(
                    imageVector = if (item.isFavorite) Icons.Default.Favorite else Outlined.Favorite,
                    contentDescription = null,
                )
            }
        }
        Text(
            stringQuantityResource(R.plurals.terms_amount, item.numberOfTerms)
        )
    }
}