package dev.mslalith.focuslauncher.feature.settingspage.settingsitems

import androidx.compose.runtime.Composable
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsItem

@Composable
internal fun HideApps(
    onClick: () -> Unit
) {
    SettingsItem(
        text = "Hide Apps",
        onClick = onClick
    )
}
