package dev.mslalith.focuslauncher.feature.homepage

import com.slack.circuit.runtime.CircuitUiState
import dev.mslalith.focuslauncher.feature.clock24.widget.ClockWidgetUiComponentState
import dev.mslalith.focuslauncher.feature.lunarcalendar.widget.LunarCalendarUiComponentState

data class HomePageState(
    val isPullDownNotificationShadeEnabled: Boolean,
    val showMoonCalendarDetailsDialog: Boolean,
    val clockWidgetUiComponentState: ClockWidgetUiComponentState,
    val lunarCalendarUiComponentState: LunarCalendarUiComponentState,
    val eventSink: (HomePageUiEvent) -> Unit
) : CircuitUiState
