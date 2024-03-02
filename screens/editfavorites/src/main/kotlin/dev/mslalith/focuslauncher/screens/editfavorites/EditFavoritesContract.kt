package dev.mslalith.focuslauncher.screens.editfavorites

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.SelectedApp
import kotlinx.collections.immutable.ImmutableList

data class EditFavoritesState(
    val favoriteApps: ImmutableList<SelectedApp>,
    val showHiddenApps: Boolean,
    val eventSink: (EditFavoritesUiEvent) -> Unit
) : CircuitUiState

sealed interface EditFavoritesUiEvent : CircuitUiEvent {
    data class AddToFavorites(val app: App) : EditFavoritesUiEvent
    data class RemoveFromFavorites(val app: App) : EditFavoritesUiEvent
    data object ClearFavorites : EditFavoritesUiEvent

    data object ToggleShowingHiddenApps : EditFavoritesUiEvent
    data object GoBack : EditFavoritesUiEvent
}
