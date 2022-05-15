package gordeev.it_dictionary.presentation.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface
        ) {
            Column(modifier = Modifier.padding(16.dp, 24.dp)) {
                Text(
                    text = stringResource(R.string.home_screen_add_to_favorite_dialog),
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row {
                    TextButton(
                        onClick = onChooseWordsClicked,
                        colors = Secondary,
                        textRes = R.string.home_screen_add_to_favorite_dialog_choose_words_button
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = onAllClicked,
                        modifier = Modifier.weight(1f),
                        colors = Primary,
                        textRes = R.string.home_screen_add_to_favorite_dialog_all_button
                    )
                }
            }
        }
    }
}