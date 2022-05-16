package gordeev.it_dictionary.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun DictionaryTheme(
    useDarkColors: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = dictionaryLightColors,
        typography = dictionaryTypography(),
        shapes = dictionaryShapes(),
        content = content
    )
}