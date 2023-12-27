package dev.mslalith.focuslauncher.feature.appdrawerpage

import com.slack.circuit.runtime.CircuitUiEvent
import dev.mslalith.focuslauncher.core.model.app.App

sealed interface AppDrawerPageUiEvent : CircuitUiEvent {
    data class UpdateSearchQuery(val query: String) : AppDrawerPageUiEvent
    data class AddToFavorites(val app: App) : AppDrawerPageUiEvent
    data class RemoveFromFavorites(val app: App) : AppDrawerPageUiEvent
    data class AddToHiddenApps(val app: App) : AppDrawerPageUiEvent
    data class UpdateDisplayName(val app: App, val displayName: String) : AppDrawerPageUiEvent
    data object ReloadIconPack : AppDrawerPageUiEvent
}
