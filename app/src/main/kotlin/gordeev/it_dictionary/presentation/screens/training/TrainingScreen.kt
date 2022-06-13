package gordeev.it_dictionary.presentation.screens.training

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import gordeev.it_dictionary.R
import gordeev.it_dictionary.presentation.ui.TextButton
import gordeev.it_dictionary.presentation.ui.TextButtonColors.Primary
import gordeev.it_dictionary.presentation.ui.TextButtonColors.Secondary
import gordeev.it_dictionary.presentation.utils.screenEdgeOffsetHorizontal

@Composable
fun TrainingScreen(
    returnToFavoriteScreen: () -> Unit,
    openTrainingScreen: (pageIndex: Int) -> Unit
) {
    TrainingScreen(
        viewModel = hiltViewModel(),
        returnToFavoriteScreen = returnToFavoriteScreen,
        openTrainingScreen = openTrainingScreen
    )
}

@Composable
private fun TrainingScreen(
    viewModel: TrainingViewModel,
    returnToFavoriteScreen: () -> Unit,
    openTrainingScreen: (pageIndex: Int) -> Unit
) {
    val state = viewModel.state
    TrainingScreen(
        returnToFavoriteScreen = returnToFavoriteScreen,
        onTermRememberClicked = {
            viewModel.setTermIsLearned(it)
            if (state.page == state.totalPages) {
                returnToFavoriteScreen()
            } else {
                openTrainingScreen(state.page)
            }
        },
        viewState = state
    )
}

@Composable
private fun TrainingScreen(
    returnToFavoriteScreen: () -> Unit,
    onTermRememberClicked: (remembered: Boolean) -> Unit,
    viewState: TrainingScreenState
) {

    val scrollState = rememberScrollState(0)
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, end = screenEdgeOffsetHorizontal),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { returnToFavoriteScreen() },
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.training_screen_back_icon_description)
                    )
                }
                Text(text = "${viewState.page}/${viewState.totalPages}")
            }
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(top = 20.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = viewState.title,
                    modifier = Modifier.padding(horizontal = screenEdgeOffsetHorizontal),
                    style = MaterialTheme.typography.h4
                )
                Text(
                    text = viewState.meaning,
                    modifier = Modifier.padding(horizontal = screenEdgeOffsetHorizontal),
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Normal),
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenEdgeOffsetHorizontal),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(
                    onClick = { onTermRememberClicked(true) },
                    modifier = Modifier.weight(1f),
                    colors = Secondary,
                    textRes = R.string.training_screen_memorized_button
                )
                TextButton(
                    onClick = { onTermRememberClicked(false) },
                    modifier = Modifier.weight(1f),
                    colors = Primary,
                    textRes = R.string.training_screen_repeat_button
                )
            }
        }
    }
}