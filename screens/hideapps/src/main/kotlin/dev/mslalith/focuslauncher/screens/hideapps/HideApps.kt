package dev.mslalith.focuslauncher.screens.hideapps

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.screens.HideAppsScreen
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.testSemantics
import dev.mslalith.focuslauncher.core.ui.AppBarWithBackIcon
import dev.mslalith.focuslauncher.core.ui.StatusBarColor
import dev.mslalith.focuslauncher.screens.hideapps.ui.HiddenAppsList
import dev.mslalith.focuslauncher.screens.hideapps.utils.TestTags

@CircuitInject(HideAppsScreen::class, SingletonComponent::class)
@Composable
fun HideApps(
    state: HideAppsState,
    modifier: Modifier = Modifier
) {
    // Need to extract the eventSink out to a local val, so that the Compose Compiler
    // treats it as stable. See: https://issuetracker.google.com/issues/256100927
    val eventSink = state.eventSink

    HideAppsScreen(
        modifier = modifier,
        state = state,
        onClearHiddenApps = { eventSink(HideAppsUiEvent.ClearHiddenApps) },
        onRemoveFromFavorites = { eventSink(HideAppsUiEvent.RemoveFromFavorites(app = it)) },
        onAddToHiddenApps = { eventSink(HideAppsUiEvent.AddToHiddenApps(app = it)) },
        onRemoveFromHiddenApps = { eventSink(HideAppsUiEvent.RemoveFromHiddenApps(app = it)) },
        goBack = { eventSink(HideAppsUiEvent.GoBack) }
    )
}

@Composable
private fun HideAppsScreen(
    state: HideAppsState,
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
            selectedHiddenApps = state.hiddenApps,
            onRemoveFromFavorites = onRemoveFromFavorites,
            onAddToHiddenApps = onAddToHiddenApps,
            onRemoveFromHiddenApps = onRemoveFromHiddenApps
        )
    }
}
