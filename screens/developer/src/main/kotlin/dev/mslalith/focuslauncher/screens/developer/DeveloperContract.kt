package dev.mslalith.focuslauncher.screens.developer

import android.net.Uri
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class DeveloperState(
    val defaultFavoritesName: String,
    val isFavoritesReading: Boolean,
    val isFavoritesSaving: Boolean,
    val eventSink: (DeveloperUiEvent) -> Unit
) : CircuitUiState

sealed interface DeveloperUiEvent : CircuitUiEvent {
    data object GoBack : DeveloperUiEvent

    data class ReadFavoritesFromUri(val uri: Uri) : DeveloperUiEvent
    data class SaveFavoritesFromUri(val uri: Uri) : DeveloperUiEvent
}
