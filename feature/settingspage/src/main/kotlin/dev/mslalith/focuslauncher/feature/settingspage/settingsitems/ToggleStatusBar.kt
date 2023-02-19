package dev.mslalith.focuslauncher.feature.settingspage.settingsitems

import androidx.compose.runtime.Composable
import dev.mslalith.focuslauncher.core.ui.providers.LocalSystemUiController
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsItem

@Composable
internal fun ToggleStatusBar(
    showStatusBar: Boolean,
    onClick: () -> Unit
) {
    val systemUiController = LocalSystemUiController.current
    val showOrHideText = when (showStatusBar) {
        true -> {
            systemUiController.isStatusBarVisible = true
            "Hide"
        }
        false -> {
            systemUiController.isStatusBarVisible = false
            "Show"
        }
    }

    SettingsItem(
        text = "$showOrHideText Status Bar",
        onClick = onClick
    )
}
