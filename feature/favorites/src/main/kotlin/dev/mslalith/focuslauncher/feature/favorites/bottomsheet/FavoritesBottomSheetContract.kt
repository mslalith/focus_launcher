package dev.mslalith.focuslauncher.feature.favorites.bottomsheet

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import dev.mslalith.focuslauncher.core.model.app.AppWithColor
import kotlinx.collections.immutable.ImmutableList

data class FavoritesBottomSheetState(
    val favoritesList: ImmutableList<AppWithColor>,
    val eventSink: (FavoritesBottomSheetUiEvent) -> Unit
) : CircuitUiState

sealed interface FavoritesBottomSheetUiEvent : CircuitUiEvent {
    data class Move(val fromIndex: Int, val toIndex: Int) : FavoritesBottomSheetUiEvent
    data class Remove(val appWithColor: AppWithColor) : FavoritesBottomSheetUiEvent
    data object Save : FavoritesBottomSheetUiEvent
}
