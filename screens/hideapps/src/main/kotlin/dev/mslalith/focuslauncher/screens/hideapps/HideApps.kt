package dev.mslalith.focuslauncher.screens.hideapps

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.screens.HideAppsScreen

@CircuitInject(HideAppsScreen::class, SingletonComponent::class)
@Composable
fun HideApps(
    state: HideAppsState,
    modifier: Modifier = Modifier
) {
    // Need to extract the eventSink out to a local val, so that the Compose Compiler
    // treats it as stable. See: https://issuetracker.google.com/issues/256100927
    val eventSink = state.eventSink

    HideAppsScreenInternal(
        modifier = modifier,
        hideAppsState = dev.mslalith.focuslauncher.screens.hideapps.model.HideAppsState(state.hiddenApps),
        onClearHiddenApps = { eventSink(HideAppsUiEvent.ClearHiddenApps) },
        onRemoveFromFavorites = { eventSink(HideAppsUiEvent.RemoveFromFavorites(app = it)) },
        onAddToHiddenApps = { eventSink(HideAppsUiEvent.AddToHiddenApps(app = it)) },
        onRemoveFromHiddenApps = { eventSink(HideAppsUiEvent.RemoveFromHiddenApps(app = it)) },
        goBack = { eventSink(HideAppsUiEvent.GoBack) }
    )
}
