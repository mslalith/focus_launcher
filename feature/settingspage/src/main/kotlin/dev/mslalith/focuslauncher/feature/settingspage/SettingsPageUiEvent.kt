package dev.mslalith.focuslauncher.feature.settingspage

import android.content.Context
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.screen.Screen

sealed interface SettingsPageUiEvent : CircuitUiEvent {
    data object ToggleStatusBarVisibility : SettingsPageUiEvent
    data object ToggleNotificationShade : SettingsPageUiEvent
    data class RefreshIsDefaultLauncher(val context: Context) : SettingsPageUiEvent
    data class GoTo(val screen: Screen) : SettingsPageUiEvent
    data class OnBottomSheetOpened(val screen: Screen) : SettingsPageUiEvent
}
