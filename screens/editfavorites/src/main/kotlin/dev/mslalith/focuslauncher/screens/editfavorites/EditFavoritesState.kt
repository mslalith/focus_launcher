package dev.mslalith.focuslauncher.screens.editfavorites

import com.slack.circuit.runtime.CircuitUiState
import dev.mslalith.focuslauncher.core.model.app.SelectedApp
import kotlinx.collections.immutable.ImmutableList

data class EditFavoritesState(
    val favoriteApps: ImmutableList<SelectedApp>,
    val showHiddenApps: Boolean,
    val eventSink: (EditFavoritesUiEvent) -> Unit
) : CircuitUiState
