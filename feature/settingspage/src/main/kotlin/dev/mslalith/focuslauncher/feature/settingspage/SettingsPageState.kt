package dev.mslalith.focuslauncher.feature.settingspage

import com.slack.circuit.runtime.CircuitUiState

data class SettingsPageState(
    val showStatusBar: Boolean,
    val canDrawNotificationShade: Boolean,
    val showIconPack: Boolean,
    val isDefaultLauncher: Boolean,
    val showDeveloperOption: Boolean,
    val eventSink: (SettingsPageUiEvent) -> Unit
) : CircuitUiState
