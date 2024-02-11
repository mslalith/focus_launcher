package dev.mslalith.focuslauncher.screens.developer

import com.slack.circuit.runtime.CircuitUiState

data class DeveloperState(
    val defaultFavoritesName: String,
    val isFavoritesReading: Boolean,
    val isFavoritesSaving: Boolean,
    val eventSink: (DeveloperUiEvent) -> Unit
) : CircuitUiState
