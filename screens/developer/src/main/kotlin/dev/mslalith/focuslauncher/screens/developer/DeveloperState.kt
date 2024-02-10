package dev.mslalith.focuslauncher.screens.developer

import com.slack.circuit.runtime.CircuitUiState

data class DeveloperState(
    val eventSink: (DeveloperUiEvent) -> Unit
) : CircuitUiState
