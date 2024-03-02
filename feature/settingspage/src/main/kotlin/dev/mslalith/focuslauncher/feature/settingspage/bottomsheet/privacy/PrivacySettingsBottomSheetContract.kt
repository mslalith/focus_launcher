package dev.mslalith.focuslauncher.feature.settingspage.bottomsheet.privacy

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class PrivacySettingsBottomSheetState(
    val reportCrashes: Boolean,
    val eventSink: (PrivacySettingsBottomSheetUiEvent) -> Unit
) : CircuitUiState

sealed interface PrivacySettingsBottomSheetUiEvent : CircuitUiEvent {
    data object ToggleReportCrashes : PrivacySettingsBottomSheetUiEvent
}
