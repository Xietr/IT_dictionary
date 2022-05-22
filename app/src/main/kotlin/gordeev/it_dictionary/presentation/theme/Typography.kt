package gordeev.it_dictionary.presentation.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun dictionaryTypography(): Typography {
    return MaterialTheme.typography
        .copy(
            h5 = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                letterSpacing = 0.3.sp
            ),
            body1 = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                letterSpacing = 0.1.sp
            ),
            button = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                letterSpacing = 0.3.sp
            )
        )
}