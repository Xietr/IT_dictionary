package gordeev.it_dictionary.presentation.screens.term_set_partial_add

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import gordeev.it_dictionary.R
import gordeev.it_dictionary.presentation.utils.screenEdgeOffsetHorizontal
import gordeev.it_dictionary.presentation.utils.screenEdgeOffsetVertical

@Preview
@Composable
fun TermSetPartialAddScreen(

) {
    val listState = rememberLazyListState()
    val scrollOffset: Float = minOf(
        1f,
        1 - (listState.firstVisibleItemScrollOffset / 600f + listState.firstVisibleItemIndex)
    )
    Scaffold(
        topBar = {
            CollapsingToolbar(scrollOffset)
        }
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = screenEdgeOffsetHorizontal)
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            LazyColumn(
                state = listState
            ) {
                items(20) { index ->
                    AddTermsFromSetItem()
                }
            }
            Button(
                modifier = Modifier
                    .padding(bottom = screenEdgeOffsetVertical)
                    .fillMaxWidth()
                    .height(60.dp),
                onClick = { /*TODO*/ }
            ) {
                Text(
                    text = stringResource(id = R.string.add_terms_from_set_button)
                )
            }
        }
    }
}

@Composable
private fun AddTermsFromSetItem() {
    Row(
        modifier = Modifier
            .drawUnderline()
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = false, onCheckedChange = {})
        Text(text = "Термин", style = MaterialTheme.typography.h6)
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

@Composable
private fun CollapsingToolbar(scrollOffset: Float) {
    val containerHeight by animateDpAsState(targetValue = max(48.dp, 128.dp * scrollOffset))
    val descriptionLines = maxOf(1f, scrollOffset * 6).toInt()
    val category = object {
        val title = "Андроид разработка"
        val description = "Небольшое описание набора, информация о том, что он содержит и кому подходит"
    }
    Row(modifier = Modifier.height(containerHeight)) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
        }
        Column {
            Text(
                text = category.title,
                style = MaterialTheme.typography.h5
            )
            Text(
                text = category.description,
                style = MaterialTheme.typography.subtitle1,
                maxLines = descriptionLines
            )
        }
    }
}