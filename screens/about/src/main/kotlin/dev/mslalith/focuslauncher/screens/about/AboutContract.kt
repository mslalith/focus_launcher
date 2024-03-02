package dev.mslalith.focuslauncher.screens.about

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class AboutState(
    val eventSink: (AboutUiEvent) -> Unit
) : CircuitUiState

sealed interface AboutUiEvent : CircuitUiEvent {
    data object GoBack : AboutUiEvent
}
