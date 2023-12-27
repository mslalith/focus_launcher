package dev.mslalith.focuslauncher.feature.clock24.bottomsheet.clockwidgetsettings

import com.slack.circuit.runtime.CircuitUiEvent
import dev.mslalith.focuslauncher.core.model.ClockAlignment

sealed interface ClockWidgetSettingsBottomSheetUiEvent : CircuitUiEvent {
    data object ToggleClock24 : ClockWidgetSettingsBottomSheetUiEvent
    data object ToggleUse24Hour : ClockWidgetSettingsBottomSheetUiEvent
    data class UpdateClockAlignment(val clockAlignment: ClockAlignment) : ClockWidgetSettingsBottomSheetUiEvent
    data class UpdateClock24AnimationDuration(val duration: Int) : ClockWidgetSettingsBottomSheetUiEvent
}
