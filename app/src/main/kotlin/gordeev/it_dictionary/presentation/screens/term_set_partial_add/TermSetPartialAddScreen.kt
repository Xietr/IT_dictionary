package gordeev.it_dictionary.presentation.screens.term_set_partial_add

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import gordeev.it_dictionary.R
import gordeev.it_dictionary.data.data_sources.local.entities.Term
import gordeev.it_dictionary.data.data_sources.local.entities.TermSet
import gordeev.it_dictionary.presentation.ui.TextButton
import gordeev.it_dictionary.presentation.ui.TextButtonColors.Primary
import gordeev.it_dictionary.presentation.utils.rememberStateWithLifecycle
import gordeev.it_dictionary.presentation.utils.screenEdgeOffsetHorizontal
import gordeev.it_dictionary.presentation.utils.screenEdgeOffsetVertical

@Composable
fun TermSetPartialAddScreen() {
    TermSetPartialAddScreen(hiltViewModel())
}

@Composable
private fun TermSetPartialAddScreen(
    viewModel: TermSetPartialAddViewModel
) {
    val termSet by rememberStateWithLifecycle(viewModel.termSet)
    termSet?.let {
        TermSetPartialAddScreen(
            it,
            onButtonClick = viewModel::onAddWordsClicked,
            onFavoriteChanged = viewModel::onFavoriteChanged
        )
    }
}

@Composable
private fun TermSetPartialAddScreen(
    termSet: TermSet,
    onButtonClick: () -> Unit,
    onFavoriteChanged: (Term, Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            Toolbar(termSet.name, termSet.description)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = screenEdgeOffsetHorizontal)
                .fillMaxSize(),
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                termSet.terms.forEach { term ->
                    item {
                        AddTermsFromSetItem(term) {
                            onFavoriteChanged(term, it)
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
private fun Toolbar(name: String, description: String) {
    val onBackPressed = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = screenEdgeOffsetHorizontal)
    ) {
        Row {
            IconButton(onClick = { onBackPressed?.onBackPressed() }) {
                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
            }
            Text(
                text = name,
                style = MaterialTheme.typography.h4
            )
        }
        Text(
            modifier = Modifier.padding(screenEdgeOffsetHorizontal),
            text = description,
            style = MaterialTheme.typography.h6,
        )
    }
}

@Composable
private fun AddTermsFromSetItem(term: Term, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .drawUnderline()
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = term.isFavorite, onCheckedChange = onCheckedChange)
        Text(text = term.name, style = MaterialTheme.typography.h6)
    }
}

private fun Modifier.drawUnderline(lineWidth: Dp = 1.dp, color: Color = Color(0xFFECECEE)): Modifier {
    return drawBehind {
        val strokeWidth = lineWidth.value * density
        val y = size.height - strokeWidth / 2
        drawLine(
            color,
            Offset(0f, y),
            Offset(size.width, y),
            strokeWidth
        )
    }
}
