package gordeev.it_dictionary.presentation.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import gordeev.it_dictionary.presentation.ui.TextButtonColors.Primary
import gordeev.it_dictionary.presentation.ui.TextButtonColors.Secondary

private val textButtonPaddings = 20.dp

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
    isLoading: Boolean = false,
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
        contentPadding = PaddingValues()
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(textButtonPaddings - 10.dp),
                color = LocalContentColor.current
            )
        } else {
            Text(
                modifier = Modifier.padding(textButtonPaddings),
                text = stringResource(id = textRes),
                style = MaterialTheme.typography.button
            )
        }
    }
}