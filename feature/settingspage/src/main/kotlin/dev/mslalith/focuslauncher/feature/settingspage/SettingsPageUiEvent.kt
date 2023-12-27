package dev.mslalith.focuslauncher.feature.settingspage

import android.content.Context
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.screen.Screen
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerIconViewType

sealed interface SettingsPageUiEvent : CircuitUiEvent {
    data object ToggleStatusBarVisibility : SettingsPageUiEvent
    data object ToggleNotificationShade : SettingsPageUiEvent
    data object ToggleSearchBarVisibility : SettingsPageUiEvent
    data object ToggleAppGroupHeaderVisibility : SettingsPageUiEvent
    data class RefreshIsDefaultLauncher(val context: Context) : SettingsPageUiEvent
    data class UpdateAppDrawerViewType(val viewType: AppDrawerViewType) : SettingsPageUiEvent
    data class UpdateAppDrawerIconViewType(val viewType: AppDrawerIconViewType) : SettingsPageUiEvent
    data class GoTo(val screen: Screen) : SettingsPageUiEvent
}
