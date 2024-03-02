package dev.mslalith.focuslauncher.feature.settingspage

import android.content.Context
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen

data class SettingsPageState(
    val showStatusBar: Boolean,
    val canDrawNotificationShade: Boolean,
    val showIconPack: Boolean,
    val isDefaultLauncher: Boolean,
    val showDeveloperOption: Boolean,
    val eventSink: (SettingsPageUiEvent) -> Unit
) : CircuitUiState

sealed interface SettingsPageUiEvent : CircuitUiEvent {
    data object ToggleStatusBarVisibility : SettingsPageUiEvent
    data object ToggleNotificationShade : SettingsPageUiEvent
    data class RefreshIsDefaultLauncher(val context: Context) : SettingsPageUiEvent
    data class GoTo(val screen: Screen) : SettingsPageUiEvent
    data class OnBottomSheetOpened(val screen: Screen) : SettingsPageUiEvent
}
