package dev.mslalith.focuslauncher.feature.appdrawerpage.bottomsheet.updateappdisplayname

import com.slack.circuit.runtime.CircuitUiEvent

sealed interface UpdateAppDisplayNameBottomSheetUiEvent : CircuitUiEvent {
    data class UpdateDisplayName(val displayName: String) : UpdateAppDisplayNameBottomSheetUiEvent
}
