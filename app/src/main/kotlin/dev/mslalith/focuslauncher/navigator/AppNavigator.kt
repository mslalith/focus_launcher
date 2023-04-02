package dev.mslalith.focuslauncher.navigator

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.mslalith.focuslauncher.core.model.Screen
import dev.mslalith.focuslauncher.core.ui.providers.LocalNavController
import dev.mslalith.focuslauncher.screens.editfavorites.EditFavoritesScreen
import dev.mslalith.focuslauncher.screens.hideapps.HideAppsScreen
import dev.mslalith.focuslauncher.ui.screens.LauncherScreen
import dev.mslalith.focuslauncher.ui.screens.PickPlaceForLunarPhaseScreen

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
        pickPlaceForLunarPhase()
    }
}

@OptIn(ExperimentalMaterialApi::class)
private fun NavGraphBuilder.launcherScreen() {
    composable(Screen.Launcher.id) {
        LauncherScreen()
    }
}

private fun NavGraphBuilder.editFavoritesScreen() {
    composable(Screen.EditFavorites.id) {
        val navController = LocalNavController.current
        EditFavoritesScreen(
            goBack = navController::popBackStack
        )
    }
}

private fun NavGraphBuilder.hideAppsScreen() {
    composable(Screen.HideApps.id) {
        val navController = LocalNavController.current
        HideAppsScreen(
            goBack = navController::popBackStack
        )
    }
}

private fun NavGraphBuilder.pickPlaceForLunarPhase() {
    composable(Screen.PickPlaceForLunarPhase.id) {
        PickPlaceForLunarPhaseScreen(
            pickPlaceViewModel = hiltViewModel(it)
        )
    }
}
