package gordeev.it_dictionary.presentation.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DictionaryTheme(
    useDarkColors: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = dictionaryLightColors,
        typography = dictionaryTypography(),
        shapes = dictionaryShapes(),
        content = {
            CompositionLocalProvider(LocalOverScrollConfiguration provides null) {
                content()
            }
        }
    )
}