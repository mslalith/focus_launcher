package dev.mslalith.focuslauncher.screens.hideapps

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            FloatingActionButton(
                onClick = hideAppsViewModel::clearHiddenApps,
                backgroundColor = MaterialTheme.colors.onBackground.copy(alpha = 0.85f),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_broom),
                    contentDescription = "Clear Hidden Apps",
                    tint = MaterialTheme.colors.background,
                )
            }
        }
    ) {
        HiddenAppsList(
            modifier = Modifier.padding(it),
            hiddenApps = hideAppsViewModel.hiddenAppsFlow.collectAsStateWithLifecycle().value,
            onRemoveFromFavorites = hideAppsViewModel::removeFromFavorites,
            onAddToHiddenApps = hideAppsViewModel::addToHiddenApps,
            onRemoveFromHiddenApps = hideAppsViewModel::removeFromHiddenApps
        )
    }
}
