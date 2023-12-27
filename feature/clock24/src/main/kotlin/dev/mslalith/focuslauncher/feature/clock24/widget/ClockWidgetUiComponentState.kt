package dev.mslalith.focuslauncher.feature.clock24.widget

import com.slack.circuit.runtime.CircuitUiState
import dev.mslalith.focuslauncher.core.model.ClockAlignment

data class ClockWidgetUiComponentState(
    val currentTime: String,
    val showClock24: Boolean,
    val use24Hour: Boolean,
    val clockAlignment: ClockAlignment,
    val clock24AnimationDuration: Int,
    val eventSink: (ClockWidgetUiComponentUiEvent) -> Unit
) : CircuitUiState
