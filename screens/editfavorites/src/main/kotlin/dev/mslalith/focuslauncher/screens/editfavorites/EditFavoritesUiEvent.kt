package dev.mslalith.focuslauncher.screens.editfavorites

import com.slack.circuit.runtime.CircuitUiEvent
import dev.mslalith.focuslauncher.core.model.app.App

sealed interface EditFavoritesUiEvent : CircuitUiEvent {
    data class AddToFavorites(val app: App) : EditFavoritesUiEvent
    data class RemoveFromFavorites(val app: App) : EditFavoritesUiEvent
    data object ClearFavorites : EditFavoritesUiEvent

    data object ToggleShowingHiddenApps : EditFavoritesUiEvent
    data object GoBack : EditFavoritesUiEvent
}
