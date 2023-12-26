package dev.mslalith.focuslauncher.feature.homepage

import com.slack.circuit.runtime.CircuitUiEvent

sealed interface HomePageUiEvent : CircuitUiEvent {
    data object ShowMoonCalendar : HomePageUiEvent
}
