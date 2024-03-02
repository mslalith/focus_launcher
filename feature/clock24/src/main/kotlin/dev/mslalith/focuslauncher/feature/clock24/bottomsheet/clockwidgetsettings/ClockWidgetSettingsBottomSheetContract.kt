package dev.mslalith.focuslauncher.feature.clock24.bottomsheet.clockwidgetsettings

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import dev.mslalith.focuslauncher.core.model.ClockAlignment

data class ClockWidgetSettingsBottomSheetState(
    val currentTime: String,
    val showClock24: Boolean,
    val use24Hour: Boolean,
    val clockAlignment: ClockAlignment,
    val clock24AnimationDuration: Int,
    val eventSink: (ClockWidgetSettingsBottomSheetUiEvent) -> Unit
) : CircuitUiState

sealed interface ClockWidgetSettingsBottomSheetUiEvent : CircuitUiEvent {
    data object ToggleClock24 : ClockWidgetSettingsBottomSheetUiEvent
    data object ToggleUse24Hour : ClockWidgetSettingsBottomSheetUiEvent
    data class UpdateClockAlignment(val clockAlignment: ClockAlignment) : ClockWidgetSettingsBottomSheetUiEvent
    data class UpdateClock24AnimationDuration(val duration: Int) : ClockWidgetSettingsBottomSheetUiEvent
}
