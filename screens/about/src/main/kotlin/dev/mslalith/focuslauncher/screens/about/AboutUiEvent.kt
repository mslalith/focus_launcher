package dev.mslalith.focuslauncher.screens.about

import com.slack.circuit.runtime.CircuitUiEvent

sealed interface AboutUiEvent : CircuitUiEvent {
    data object GoBack : AboutUiEvent
}
