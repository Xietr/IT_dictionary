package gordeev.it_dictionary.presentation.utils

import android.content.res.Resources
import androidx.annotation.PluralsRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

@Composable
@ReadOnlyComposable
fun stringQuantityResource(@PluralsRes id: Int, quantity: Int): String {
    val resources = resources()
    return resources.getQuantityString(id, quantity, quantity)
}

//duplication of androidx.compose.ui.res.resources()
@Composable
@ReadOnlyComposable
private fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}
