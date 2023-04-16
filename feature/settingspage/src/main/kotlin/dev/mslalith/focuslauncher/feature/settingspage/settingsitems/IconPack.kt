package dev.mslalith.focuslauncher.feature.settingspage.settingsitems

import androidx.compose.runtime.Composable
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsItem

@Composable
internal fun IconPack(
    onClick: () -> Unit
) {
    SettingsItem(
        text = "Icon Pack",
        onClick = onClick
    )
}
