package dev.mslalith.focuslauncher.screens.hideapps

import com.slack.circuit.runtime.CircuitUiEvent
import dev.mslalith.focuslauncher.core.model.app.App

sealed interface HideAppsUiEvent : CircuitUiEvent {
    data class AddToHiddenApps(val app: App) : HideAppsUiEvent
    data class RemoveFromHiddenApps(val app: App) : HideAppsUiEvent
    data object ClearHiddenApps : HideAppsUiEvent

    data class RemoveFromFavorites(val app: App) : HideAppsUiEvent
    data object GoBack : HideAppsUiEvent
}
