package dev.mslalith.focuslauncher.feature.clock24.bottomsheet.clockwidgetsettings

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
