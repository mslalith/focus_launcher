package dev.mslalith.focuslauncher.feature.settingspage.settingsitems

import androidx.compose.runtime.Composable
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsItem

@Composable
internal fun AppDrawer(
    onClick: () -> Unit
) {
    SettingsItem(
        text = "App Drawer",
        onClick = onClick
    )
}
