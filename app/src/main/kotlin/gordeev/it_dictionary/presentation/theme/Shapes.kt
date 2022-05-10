package gordeev.it_dictionary.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun dictionaryShapes() = MaterialTheme.shapes.copy(
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(16.dp)
)