package dev.mslalith.focuslauncher.screens.hideapps

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.SelectedHiddenApp
import kotlinx.collections.immutable.ImmutableList

data class HideAppsState(
    val hiddenApps: ImmutableList<SelectedHiddenApp>,
    val eventSink: (HideAppsUiEvent) -> Unit
) : CircuitUiState

sealed interface HideAppsUiEvent : CircuitUiEvent {
    data class AddToHiddenApps(val app: App) : HideAppsUiEvent
    data class RemoveFromHiddenApps(val app: App) : HideAppsUiEvent
    data object ClearHiddenApps : HideAppsUiEvent

    data class RemoveFromFavorites(val app: App) : HideAppsUiEvent
    data object GoBack : HideAppsUiEvent
}
