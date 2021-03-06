@file:OptIn(ExperimentalAnimationApi::class)

package gordeev.it_dictionary.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import gordeev.it_dictionary.presentation.screens.favorite.FavoriteScreen
import gordeev.it_dictionary.presentation.screens.home.HomeScreen
import gordeev.it_dictionary.presentation.screens.suggest.SuggestScreen
import gordeev.it_dictionary.presentation.screens.term_search.TermSearchScreen
import gordeev.it_dictionary.presentation.screens.term_set_partial_add.TermSetPartialAddScreen
import gordeev.it_dictionary.presentation.screens.training.TrainingScreen

internal sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Suggest : Screen("suggest")
    object Favorite : Screen("favorite")
}

const val termSetPartialAddArg = "termSetPartialAdd"
const val trainingArg = "training"

private sealed class LeafScreen(
    private val route: String,
) {
    fun getRoute(root: Screen) = "${root.route}/$route"

    object Home : LeafScreen("home")
    object Suggest : LeafScreen("suggest")
    object Favorite : LeafScreen("favorite")
    object Training : LeafScreen("training/{$trainingArg}") {
        fun createRoute(root: Screen, page: Int): String {
            return "${root.route}/training/$page"
        }
    }

    object Search : LeafScreen("search")

    object TermSetPartialAddToFavorite : LeafScreen("termSet/{$termSetPartialAddArg}") {
        fun createRoute(root: Screen, termSetId: String): String {
            return "${root.route}/termSet/$termSetId"
        }
    }
}

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
        addSuggestingTopLevel()
        addFavoriteTopLevel(navController)
    }
}

private fun NavGraphBuilder.addHomeTopLevel(
    navController: NavController
) {
    navigation(
        route = Screen.Home.route,
        startDestination = LeafScreen.Home.getRoute(Screen.Home),
    ) {
        addHomeScreen(navController, Screen.Home)
        addTermSearchScreen(Screen.Home)
        addTermsFromSetToFavorite(Screen.Home)
    }
}

private fun NavGraphBuilder.addSuggestingTopLevel() {
    navigation(
        route = Screen.Suggest.route,
        startDestination = LeafScreen.Suggest.getRoute(Screen.Suggest),
    ) {
        addSuggestScreen(Screen.Suggest)
    }
}

private fun NavGraphBuilder.addFavoriteTopLevel(
    navController: NavController
) {
    navigation(
        route = Screen.Favorite.route,
        startDestination = LeafScreen.Favorite.getRoute(Screen.Favorite),
    ) {
        addFavoriteScreen(Screen.Favorite, navController)
        addTrainingScreen(Screen.Favorite, navController)
    }
}

private fun NavGraphBuilder.addHomeScreen(
    navController: NavController,
    root: Screen,
) {
    composable(
        route = LeafScreen.Home.getRoute(root)
    ) {
        HomeScreen(
            openPartialAddFromSet = {
                navController.navigate(LeafScreen.TermSetPartialAddToFavorite.createRoute(root, it))
            },
            openSearchScreen = {
                navController.navigate(LeafScreen.Search.getRoute(root))
            }
        )
    }
}

private fun NavGraphBuilder.addTermSearchScreen(
    root: Screen
) {
    composable(
        route = LeafScreen.Search.getRoute(root)
    ) {
        TermSearchScreen()
    }
}

private fun NavGraphBuilder.addTermsFromSetToFavorite(
    root: Screen,
) {
    composable(
        route = LeafScreen.TermSetPartialAddToFavorite.getRoute(root),
        arguments = listOf(
            navArgument(termSetPartialAddArg) { type = NavType.StringType }
        ),
    ) {
        TermSetPartialAddScreen()
    }
}

private fun NavGraphBuilder.addSuggestScreen(
    root: Screen,
) {
    composable(
        route = LeafScreen.Suggest.getRoute(root)
    ) {
        SuggestScreen()
    }
}

private fun NavGraphBuilder.addFavoriteScreen(
    root: Screen,
    navController: NavController
) {
    composable(
        route = LeafScreen.Favorite.getRoute(root)
    ) {
        FavoriteScreen(
            openTrainingScreen = {
                navController.navigate(
                    LeafScreen.Training.createRoute(
                        root,
                        0
                    )
                )
            }
        )
    }
}

private fun NavGraphBuilder.addTrainingScreen(
    root: Screen,
    navController: NavController
) {
    composable(
        route = LeafScreen.Training.getRoute(root),
        arguments = listOf(
            navArgument(trainingArg) { type = NavType.IntType }
        ),
    ) {
        TrainingScreen(
            returnToFavoriteScreen = {
                navController.popBackStack(
                    route = LeafScreen.Favorite.getRoute(Screen.Favorite),
                    inclusive = false
                )
            },
            openTrainingScreen = {
                navController.navigate(LeafScreen.Training.createRoute(root, it))
            }
        )
    }
}