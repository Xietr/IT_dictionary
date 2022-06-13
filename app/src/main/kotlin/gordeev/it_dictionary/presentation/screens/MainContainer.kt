package gordeev.it_dictionary.presentation.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.DriveFileRenameOutline
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.BottomNavigation
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import gordeev.it_dictionary.R
import gordeev.it_dictionary.presentation.AppNavigation
import gordeev.it_dictionary.presentation.Screen
import gordeev.it_dictionary.presentation.Screen.Favorite
import gordeev.it_dictionary.presentation.Screen.Suggest
import gordeev.it_dictionary.presentation.screens.HomeNavigationItem.ImageVectorIcon
import gordeev.it_dictionary.presentation.screens.HomeNavigationItem.ResourceIcon

@Preview
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun MainContainer() {
    val navController = rememberAnimatedNavController()

    Scaffold(
        bottomBar = {
            val currentSelectedItem by navController.currentScreenAsState()
            BottomNavigation(
                selectedScreen = currentSelectedItem,
                onNavigationSelected = { selected ->
                    navController.navigate(selected.route) {
                        launchSingleTop = true
                        restoreState = true

                        if (navController.currentDestination?.route?.contains(selected.route) == true) {
                            popUpTo("${selected.route}/${selected.route}")
                        } else {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) {
        AppNavigation(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        )
    }
}

/**
 * Adds an [NavController.OnDestinationChangedListener] to this [NavController] and updates the
 * returned [State] which is updated as the destination changes.
 */
@Stable
@Composable
private fun NavController.currentScreenAsState(): State<Screen> {
    val selectedItem = remember { mutableStateOf<Screen>(Screen.Home) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == Screen.Home.route } -> {
                    selectedItem.value = Screen.Home
                }
                destination.hierarchy.any { it.route == Screen.Suggest.route } -> {
                    selectedItem.value = Screen.Suggest
                }
                destination.hierarchy.any { it.route == Screen.Favorite.route } -> {
                    selectedItem.value = Screen.Favorite
                }
            }
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}

@Composable
private fun BottomNavigation(
    selectedScreen: Screen,
    onNavigationSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier,
) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = contentColorFor(MaterialTheme.colors.surface),
        contentPadding = rememberInsetsPaddingValues(
            insets = LocalWindowInsets.current.navigationBars
        ),
        modifier = modifier
    ) {
        HomeNavigationItems.forEach { item ->
            BottomNavigationItem(
                icon = {
                    HomeNavigationItemIcon(
                        item = item,
                        selected = selectedScreen == item.screen
                    )
                },
                label = { Text(text = stringResource(item.labelResId)) },
                selected = selectedScreen == item.screen,
                onClick = { onNavigationSelected(item.screen) },
            )
        }
    }
}

@Composable
private fun HomeNavigationItemIcon(item: HomeNavigationItem, selected: Boolean) {
    val painter = when (item) {
        is ResourceIcon -> painterResource(item.iconResId)
        is ImageVectorIcon -> rememberVectorPainter(item.iconImageVector)
    }
    val selectedPainter = when (item) {
        is ResourceIcon -> painterResource(item.selectedIconResId)
        is ImageVectorIcon -> rememberVectorPainter(item.selectedImageVector)
    }

    Crossfade(targetState = selected) {
        Icon(
            painter = if (it) selectedPainter else painter,
            contentDescription = null,
        )
    }
}

private sealed class HomeNavigationItem(
    val screen: Screen,
    @StringRes val labelResId: Int,
) {
    class ResourceIcon(
        screen: Screen,
        @StringRes labelResId: Int,
        @DrawableRes val iconResId: Int,
        @DrawableRes val selectedIconResId: Int,
    ) : HomeNavigationItem(screen, labelResId)

    class ImageVectorIcon(
        screen: Screen,
        @StringRes labelResId: Int,
        val iconImageVector: ImageVector,
        val selectedImageVector: ImageVector,
    ) : HomeNavigationItem(screen, labelResId)
}

private val HomeNavigationItems = listOf(
    ImageVectorIcon(
        screen = Screen.Home,
        labelResId = R.string.bottom_navigation_home,
        iconImageVector = Outlined.Home,
        selectedImageVector = Icons.Default.Home,
    ),
    ImageVectorIcon(
        screen = Suggest,
        labelResId = R.string.bottom_navigation_suggest,
        iconImageVector = Outlined.DriveFileRenameOutline,
        selectedImageVector = Icons.Default.DriveFileRenameOutline,
    ),
    ImageVectorIcon(
        screen = Favorite,
        labelResId = R.string.bottom_navigation_favorite,
        iconImageVector = Outlined.Favorite,
        selectedImageVector = Icons.Default.Favorite,
    ),
)
