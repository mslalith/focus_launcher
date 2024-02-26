package dev.mslalith.focuslauncher.feature.favorites

import com.slack.circuit.runtime.CircuitUiEvent

sealed interface FavoritesListUiComponentUiEvent : CircuitUiEvent {
    data object AddDefaultAppsIfRequired : FavoritesListUiComponentUiEvent
}
