package dev.mslalith.focuslauncher.screens.hideapps

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.ui.AppBarWithBackIcon
import dev.mslalith.focuslauncher.screens.hideapps.ui.HiddenAppsList

@Composable
fun HideAppsScreen(
    goBack: () -> Unit
) {
    HideAppsScreen(
        hideAppsViewModel = hiltViewModel(),
        goBack = goBack
    )
}

@Composable
internal fun HideAppsScreen(
    hideAppsViewModel: HideAppsViewModel,
    goBack: () -> Unit
) {
    Scaffold(
        topBar = {
            AppBarWithBackIcon(
                title = "Hide Apps",
                onBackPressed = goBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = hideAppsViewModel::clearHiddenApps
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_broom),
                    contentDescription = "Clear Hidden Apps"
                )
            }
        }
    ) { paddingValues ->
        HiddenAppsList(
            contentPadding = paddingValues,
            hiddenApps = hideAppsViewModel.hiddenAppsFlow.collectAsStateWithLifecycle().value,
            onRemoveFromFavorites = hideAppsViewModel::removeFromFavorites,
            onAddToHiddenApps = hideAppsViewModel::addToHiddenApps,
            onRemoveFromHiddenApps = hideAppsViewModel::removeFromHiddenApps
        )
    }
}
