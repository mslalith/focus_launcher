package dev.mslalith.focuslauncher.screens.hideapps

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.testSemantics
import dev.mslalith.focuslauncher.core.ui.AppBarWithBackIcon
import dev.mslalith.focuslauncher.core.ui.StatusBarColor
import dev.mslalith.focuslauncher.screens.hideapps.model.HideAppsState
import dev.mslalith.focuslauncher.screens.hideapps.ui.HiddenAppsList
import dev.mslalith.focuslauncher.screens.hideapps.utils.TestTags

@Composable
fun HideAppsScreen(
    goBack: () -> Unit
) {
    HideAppsScreenInternal(
        goBack = goBack
    )
}

@Composable
internal fun HideAppsScreenInternal(
    hideAppsViewModel: HideAppsViewModel = hiltViewModel(),
    goBack: () -> Unit
) {
    val hideAppsState by hideAppsViewModel.hideAppsState.collectAsStateWithLifecycle()

    HideAppsScreenInternal(
        hideAppsState = hideAppsState,
        onClearHiddenApps = hideAppsViewModel::clearHiddenApps,
        onRemoveFromFavorites = hideAppsViewModel::removeFromFavorites,
        onAddToHiddenApps = hideAppsViewModel::addToHiddenApps,
        onRemoveFromHiddenApps = hideAppsViewModel::removeFromHiddenApps,
        goBack = goBack
    )
}

@Composable
internal fun HideAppsScreenInternal(
    hideAppsState: HideAppsState,
    onClearHiddenApps: () -> Unit,
    onRemoveFromFavorites: (App) -> Unit,
    onAddToHiddenApps: (App) -> Unit,
    onRemoveFromHiddenApps: (App) -> Unit,
    goBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    StatusBarColor()

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            AppBarWithBackIcon(
                title = stringResource(id = R.string.hide_apps),
                onBackPressed = goBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onClearHiddenApps,
                modifier = Modifier.testSemantics(tag = TestTags.TAG_CLEAR_HIDDEN_APPS_FAB)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_broom),
                    contentDescription = stringResource(id = R.string.clear_hidden_apps)
                )
            }
        }
    ) { paddingValues ->
        HiddenAppsList(
            contentPadding = paddingValues,
            selectedHiddenApps = hideAppsState.hiddenApps,
            onRemoveFromFavorites = onRemoveFromFavorites,
            onAddToHiddenApps = onAddToHiddenApps,
            onRemoveFromHiddenApps = onRemoveFromHiddenApps
        )
    }
}
