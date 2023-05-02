package dev.mslalith.focuslauncher.feature.settingspage.settingsitems

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import dev.mslalith.focuslauncher.core.ui.providers.LocalSystemUiController
import dev.mslalith.focuslauncher.feature.settingspage.R
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

    val stringRes = when (showStatusBar) {
        true -> R.string.hide_status_bar
        false -> R.string.show_status_bar
    }

    SettingsItem(
        text = stringResource(id = stringRes),
        onClick = onClick
    )
}
