package dev.mslalith.focuslauncher.feature.settingspage.model

import androidx.compose.runtime.Immutable

@Immutable
internal data class SettingsState(
    val showStatusBar: Boolean,
    val canDrawNotificationShade: Boolean,
    val showIconPack: Boolean,
    val isDefaultLauncher: Boolean
)
