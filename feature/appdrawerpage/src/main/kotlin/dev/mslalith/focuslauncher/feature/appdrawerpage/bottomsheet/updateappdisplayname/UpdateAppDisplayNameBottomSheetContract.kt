package dev.mslalith.focuslauncher.feature.appdrawerpage.bottomsheet.updateappdisplayname

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import dev.mslalith.focuslauncher.core.model.app.App

data class UpdateAppDisplayNameBottomSheetState(
    val app: App,
    val eventSink: (UpdateAppDisplayNameBottomSheetUiEvent) -> Unit
) : CircuitUiState

sealed interface UpdateAppDisplayNameBottomSheetUiEvent : CircuitUiEvent {
    data class UpdateDisplayName(val displayName: String) : UpdateAppDisplayNameBottomSheetUiEvent
}
