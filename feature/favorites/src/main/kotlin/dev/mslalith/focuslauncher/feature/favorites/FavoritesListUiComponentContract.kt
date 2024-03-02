package dev.mslalith.focuslauncher.feature.favorites

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import dev.mslalith.focuslauncher.core.model.app.AppWithColor
import kotlinx.collections.immutable.ImmutableList

data class FavoritesListUiComponentState(
    val favoritesList: ImmutableList<AppWithColor>,
    val eventSink: (FavoritesListUiComponentUiEvent) -> Unit
) : CircuitUiState

sealed interface FavoritesListUiComponentUiEvent : CircuitUiEvent {
    data object AddDefaultAppsIfRequired : FavoritesListUiComponentUiEvent
}
