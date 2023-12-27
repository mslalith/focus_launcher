package dev.mslalith.focuslauncher.feature.favorites

import com.slack.circuit.runtime.CircuitUiEvent
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.feature.favorites.model.FavoritesContextMode

sealed interface FavoritesListUiComponentUiEvent : CircuitUiEvent {
    data object AddDefaultAppsIfRequired : FavoritesListUiComponentUiEvent
    data class RemoveFromFavorites(val app: App) : FavoritesListUiComponentUiEvent
    data class ReorderFavorite(val app: App, val withApp: App, val onReordered: () -> Unit) : FavoritesListUiComponentUiEvent
    data class UpdateFavoritesContextMode(val mode: FavoritesContextMode) : FavoritesListUiComponentUiEvent
}
