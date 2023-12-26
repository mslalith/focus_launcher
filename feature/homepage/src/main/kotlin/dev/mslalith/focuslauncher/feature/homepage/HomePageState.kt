package dev.mslalith.focuslauncher.feature.homepage

import com.slack.circuit.runtime.CircuitUiState

data class HomePageState(
    val isPullDownNotificationShadeEnabled: Boolean,
    val showMoonCalendarDetailsDialog: Boolean,
    val eventSink: (HomePageUiEvent) -> Unit
) : CircuitUiState
