package dev.mslalith.focuslauncher.screens.about

import com.slack.circuit.runtime.CircuitUiState

data class AboutState(
    val eventSink: (AboutUiEvent) -> Unit
) : CircuitUiState
