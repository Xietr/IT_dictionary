package gordeev.it_dictionary.presentation.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val screenEdgeOffsetHorizontal = 16.dp
val screenEdgeOffsetVertical = 16.dp

fun Modifier.screenEdgeOffsetPaddings() = padding(horizontal = screenEdgeOffsetHorizontal, vertical = screenEdgeOffsetVertical)