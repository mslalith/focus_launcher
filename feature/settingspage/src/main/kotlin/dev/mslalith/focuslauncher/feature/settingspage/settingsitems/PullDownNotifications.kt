package dev.mslalith.focuslauncher.feature.settingspage.settingsitems

import androidx.compose.runtime.Composable
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsItem

@Composable
internal fun PullDownNotifications(
    enableNotificationShade: Boolean,
    onClick: () -> Unit
) {
    val enableOrDisableText = if (enableNotificationShade) "Disable" else "Enable"
    SettingsItem(
        text = "$enableOrDisableText Pull down Notifications",
        onClick = onClick
    )
}
