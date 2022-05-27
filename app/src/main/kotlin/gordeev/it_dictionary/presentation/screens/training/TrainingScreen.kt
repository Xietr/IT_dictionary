package gordeev.it_dictionary.presentation.screens.training

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gordeev.it_dictionary.R
import gordeev.it_dictionary.presentation.ui.TextButton
import gordeev.it_dictionary.presentation.ui.TextButtonColors.Primary
import gordeev.it_dictionary.presentation.ui.TextButtonColors.Secondary
import gordeev.it_dictionary.presentation.utils.screenEdgeOffsetHorizontal

@Preview
@Composable
fun TrainingScreen() {
    val title = "JDK"
    val meaning =
        "Java Development Kit — бесплатно распространяемый компанией Oracle Corporation комплект разработчика приложений на языке Java, включающий в себя компилятор Java, стандартные библиотеки классов Java, примеры, документацию, различные утилиты и исполнительную систему Java."

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
                    onClick = { /*TODO*/ },
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.training_screen_back_icon_description)
                    )
                }
                Text(text = "1/16") //todo
            }
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(top = 20.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = title,
                    modifier = Modifier.padding(horizontal = screenEdgeOffsetHorizontal),
                    style = MaterialTheme.typography.h4
                )
                Text(
                    text = meaning,
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
                    onClick = { /*TODO*/ },
                    modifier = Modifier.weight(1f),
                    colors = Secondary,
                    textRes = R.string.training_screen_memorized_button
                )
                TextButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.weight(1f),
                    colors = Primary,
                    textRes = R.string.training_screen_repeat_button
                )
            }
        }
    }
}