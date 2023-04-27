package dev.mslalith.focuslauncher.navigator

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.mslalith.focuslauncher.core.model.Screen
import dev.mslalith.focuslauncher.core.ui.providers.LocalNavController
import dev.mslalith.focuslauncher.screens.about.AboutScreen
import dev.mslalith.focuslauncher.screens.currentplace.CurrentPlaceScreen
import dev.mslalith.focuslauncher.screens.editfavorites.EditFavoritesScreen
import dev.mslalith.focuslauncher.screens.hideapps.HideAppsScreen
import dev.mslalith.focuslauncher.screens.iconpack.IconPackScreen
import dev.mslalith.focuslauncher.screens.launcher.LauncherScreen
import dev.mslalith.focuslauncher.ui.views.MainContent

@Composable
fun AppNavigator() {
    val navController = LocalNavController.current

    NavHost(
        navController = navController,
        startDestination = Screen.Launcher.id
    ) {
        launcherScreen()
        editFavoritesScreen()
        hideAppsScreen()
        currentPlaceScreen()
        iconPackScreen()
        aboutScreen()
    }
}

private fun NavGraphBuilder.launcherScreen() {
    composable(route = Screen.Launcher.id) {
        MainContent {
            LauncherScreen()
        }
    }
}

private fun NavGraphBuilder.editFavoritesScreen() {
    composable(route = Screen.EditFavorites.id) {
        val navController = LocalNavController.current
        EditFavoritesScreen(
            goBack = navController::popBackStack
        )
    }
}

private fun NavGraphBuilder.hideAppsScreen() {
    composable(route = Screen.HideApps.id) {
        val navController = LocalNavController.current
        HideAppsScreen(
            goBack = navController::popBackStack
        )
    }
}

private fun NavGraphBuilder.currentPlaceScreen() {
    composable(route = Screen.CurrentPlace.id) {
        val navController = LocalNavController.current
        CurrentPlaceScreen(
            goBack = navController::popBackStack
        )
    }
}

private fun NavGraphBuilder.iconPackScreen() {
    composable(route = Screen.IconPack.id) {
        val navController = LocalNavController.current
        IconPackScreen(
            goBack = navController::popBackStack
        )
    }
}

private fun NavGraphBuilder.aboutScreen() {
    composable(route = Screen.About.id) {
        val navController = LocalNavController.current
        AboutScreen(
            goBack = navController::popBackStack
        )
    }
}
