package gordeev.it_dictionary.presentation.screens.home

import androidx.compose.foundation.layout.Row
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import gordeev.it_dictionary.R
import gordeev.it_dictionary.presentation.ui.TextButton
import gordeev.it_dictionary.presentation.ui.TextButtonColors.Primary
import gordeev.it_dictionary.presentation.ui.TextButtonColors.Secondary

@Preview
@Composable
fun HomeScreenDialog(
    onDismissRequest: () -> Unit = {},
    onChooseWordsClicked: () -> Unit = {},
    onAllClicked: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        text = {
            Text(text = stringResource(R.string.home_screen_add_to_favorite_dialog))
        },
        buttons = {
            Row {
                TextButton(
                    onClick = onChooseWordsClicked,
                    modifier = Modifier.weight(1f),
                    colors = Secondary,
                    textRes = R.string.home_screen_add_to_favorite_dialog_choose_words_button
                )
                TextButton(
                    onClick = onAllClicked,
                    colors = Primary,
                    textRes = R.string.home_screen_add_to_favorite_dialog_all_button
                )
            }
        }
    )
}