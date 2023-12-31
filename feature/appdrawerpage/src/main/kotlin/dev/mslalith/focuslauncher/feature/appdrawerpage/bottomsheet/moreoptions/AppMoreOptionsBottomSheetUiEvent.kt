package dev.mslalith.focuslauncher.feature.appdrawerpage.bottomsheet.moreoptions

import com.slack.circuit.runtime.CircuitUiEvent
import dev.mslalith.focuslauncher.core.model.app.App

sealed interface AppMoreOptionsBottomSheetUiEvent : CircuitUiEvent {
    data object GoBack : AppMoreOptionsBottomSheetUiEvent
    data class AddToFavorites(val app: App) : AppMoreOptionsBottomSheetUiEvent
    data class RemoveFromFavorites(val app: App) : AppMoreOptionsBottomSheetUiEvent
    data class AddToHiddenApps(val app: App, val removeFromFavorites: Boolean) : AppMoreOptionsBottomSheetUiEvent
    data class ClickUpdateDisplayName(val app: App) : AppMoreOptionsBottomSheetUiEvent
}
