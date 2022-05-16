package gordeev.it_dictionary.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import gordeev.it_dictionary.presentation.screens.home.HomeScreen
import gordeev.it_dictionary.presentation.screens.suggest.SuggestScreen
import gordeev.it_dictionary.presentation.screens.term_set_partial_add.TermSetPartialAddScreen

internal sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Suggest : Screen("suggest")
    object Favorite : Screen("favorite")
}

const val termSetPartialAddArg = "termSetPartialAdd"

private sealed class LeafScreen(
    private val route: String,
) {
    fun createRoute(root: Screen) = "${root.route}/$route"

    object Home : LeafScreen("home")
    object Suggest : LeafScreen("suggest")
    object Favorite : LeafScreen("favorite")

    object TermSetPartialAddToFavorite : LeafScreen("termSet/{$termSetPartialAddArg}") {
        fun createRoute(root: Screen, termSetId: String): String {
            return "${root.route}/termSet/$termSetId"
        }
    }
}

@ExperimentalAnimationApi
@Composable
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        enterTransition = { fadeIn(animationSpec = tween()) },
        exitTransition = { fadeOut(animationSpec = tween()) },
        modifier = modifier,
    ) {
        addHomeTopLevel(navController)
        addSuggestingTopLevel(navController)
        addFavoriteTopLevel(navController)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addHomeTopLevel(
    navController: NavController
) {
    navigation(
        route = Screen.Home.route,
        startDestination = LeafScreen.Home.createRoute(Screen.Home),
    ) {
        addHomeScreen(navController, Screen.Home)
        addTermsFromSetToFavorite(navController, Screen.Home)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addSuggestingTopLevel(
    navController: NavController
) {
    navigation(
        route = Screen.Suggest.route,
        startDestination = LeafScreen.Suggest.createRoute(Screen.Suggest),
    ) {
        addSuggestScreen(navController, Screen.Suggest)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addFavoriteTopLevel(
    navController: NavController
) {
    navigation(
        route = Screen.Favorite.route,
        startDestination = LeafScreen.Favorite.createRoute(Screen.Favorite),
    ) {

    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addHomeScreen(
    navController: NavController,
    root: Screen,
) {
    composable(
        route = LeafScreen.Home.createRoute(root)
    ) {
        HomeScreen {
            navController.navigate(LeafScreen.TermSetPartialAddToFavorite.createRoute(root, it))
        }
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addTermsFromSetToFavorite(
    navController: NavController,
    root: Screen,
) {
    composable(
        route = LeafScreen.TermSetPartialAddToFavorite.createRoute(root),
        arguments = listOf(
            navArgument(termSetPartialAddArg) { type = NavType.StringType }
        ),
    ) {
        TermSetPartialAddScreen()
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addSuggestScreen(
    navController: NavController,
    root: Screen,
) {
    composable(
        route = LeafScreen.Suggest.createRoute(root)
    ) {
        SuggestScreen()
    }
}