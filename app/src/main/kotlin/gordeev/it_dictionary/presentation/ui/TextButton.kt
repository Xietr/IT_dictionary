package gordeev.it_dictionary.presentation.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import gordeev.it_dictionary.presentation.ui.TextButtonColors.Primary
import gordeev.it_dictionary.presentation.ui.TextButtonColors.Secondary

private val textButtonPaddings = PaddingValues(20.dp)

enum class TextButtonColors {
    Primary,
    Secondary
}

@Composable
fun TextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: TextButtonColors,
    @StringRes textRes: Int
) {
    val textButtonColors = when (colors) {
        Primary -> {
            val backgroundColor = MaterialTheme.colors.primary
            ButtonDefaults.textButtonColors(
                backgroundColor = backgroundColor,
                contentColor = MaterialTheme.colors.contentColorFor(backgroundColor)
            )
        }
        Secondary -> {
            val backgroundColor = MaterialTheme.colors.secondary
            ButtonDefaults.textButtonColors(
                backgroundColor = backgroundColor,
                contentColor = MaterialTheme.colors.contentColorFor(backgroundColor)
            )
        }
    }
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        colors = textButtonColors,
        contentPadding = textButtonPaddings
    ) {
        Text(
            text = stringResource(id = textRes),
            style = MaterialTheme.typography.h6
        )
    }
}