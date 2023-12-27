package dev.mslalith.focuslauncher.feature.homepage

import com.slack.circuit.runtime.CircuitUiState
import dev.mslalith.focuslauncher.feature.clock24.widget.ClockWidgetUiComponentState

data class HomePageState(
    val isPullDownNotificationShadeEnabled: Boolean,
    val showMoonCalendarDetailsDialog: Boolean,
    val clockWidgetUiComponentState: ClockWidgetUiComponentState,
    val eventSink: (HomePageUiEvent) -> Unit
) : CircuitUiState
