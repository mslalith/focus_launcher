package dev.mslalith.focuslauncher.feature.clock24.widget

import com.slack.circuit.runtime.CircuitUiEvent

sealed interface ClockWidgetUiComponentUiEvent : CircuitUiEvent {
    data object RefreshTime : ClockWidgetUiComponentUiEvent
}
