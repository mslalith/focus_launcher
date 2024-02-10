package dev.mslalith.focuslauncher.screens.developer

import com.slack.circuit.runtime.CircuitUiEvent

sealed interface DeveloperUiEvent : CircuitUiEvent {
    data object GoBack : DeveloperUiEvent
}
