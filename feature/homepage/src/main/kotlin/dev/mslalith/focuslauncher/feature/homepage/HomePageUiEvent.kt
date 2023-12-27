package dev.mslalith.focuslauncher.feature.homepage

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.screen.Screen

sealed interface HomePageUiEvent : CircuitUiEvent {
    data class OpenBottomSheet(val screen: Screen) : HomePageUiEvent
}
