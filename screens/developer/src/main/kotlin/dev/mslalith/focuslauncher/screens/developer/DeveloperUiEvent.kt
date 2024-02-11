package dev.mslalith.focuslauncher.screens.developer

import android.net.Uri
import com.slack.circuit.runtime.CircuitUiEvent

sealed interface DeveloperUiEvent : CircuitUiEvent {
    data object GoBack : DeveloperUiEvent

    data class ReadFavoritesFromUri(val uri: Uri) : DeveloperUiEvent
    data class SaveFavoritesFromUri(val uri: Uri) : DeveloperUiEvent
}
