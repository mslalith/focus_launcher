package dev.mslalith.focuslauncher.feature.settingspage.settingsitems

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import dev.mslalith.focuslauncher.core.ui.providers.LocalSystemUiController
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsItem

@Composable
internal fun ToggleStatusBar(
    showStatusBar: Boolean,
    onClick: () -> Unit
) {
    val systemUiController = LocalSystemUiController.current

    LaunchedEffect(key1 = showStatusBar) {
        systemUiController.isStatusBarVisible = showStatusBar
    }

    val showOrHideText = when (showStatusBar) {
        true -> "Hide"
        false -> "Show"
    }

    SettingsItem(
        text = "$showOrHideText Status Bar",
        onClick = onClick
    )
}
