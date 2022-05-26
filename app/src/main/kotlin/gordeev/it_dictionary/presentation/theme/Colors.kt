package gordeev.it_dictionary.presentation.theme

import androidx.compose.material.CheckboxColors
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Text10 = Color(0xFFFFFFFF)
val Text20 = Color(0xFF999A9B)
private val Text30 = Color(0xFF05131B)
private val Bg10 = Color(0xFFFFFFFF)
val Bg20 = Color(0xFFF6F6F8)
private val Bg30 = Color(0xFFECECEE)

val dictionaryLightColors = lightColors(
    primary = Color(0xFF4A3AFF),
    onPrimary = Color.White,
    secondary = Bg20,
    onSecondary = Text30,
)

val checkboxColors: CheckboxColors
    @Composable get() = CheckboxDefaults.colors(
        checkedColor = Bg30,
        uncheckedColor = Bg30,
        checkmarkColor = Color.Black
    )