package dev.mslalith.focuslauncher.screens.hideapps

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import dev.mslalith.focuslauncher.core.ui.AppBarWithBackIcon
import dev.mslalith.focuslauncher.core.ui.ExtendedMiniFab
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
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            AppBarWithBackIcon(
                title = "Hide Apps",
                onBackPressed = goBack
            )
        },
        floatingActionButton = {
            ExtendedMiniFab(
                text = "Clear Hidden Apps",
                icon = Icons.Rounded.Refresh,
                onClick = hideAppsViewModel::clearHiddenApps
            )
        }
    ) {
        HiddenAppsList(
            modifier = Modifier.padding(it),
            hiddenApps = hideAppsViewModel.hiddenAppsFlow.collectAsState().value,
            onRemoveFromFavorites = hideAppsViewModel::removeFromFavorites,
            onAddToHiddenApps = hideAppsViewModel::addToHiddenApps,
            onRemoveFromHiddenApps = hideAppsViewModel::removeFromHiddenApps
        )
    }
}
