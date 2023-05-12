package dev.mslalith.focuslauncher.feature.settingspage.settingsitems

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.mslalith.focuslauncher.core.ui.providers.LocalLauncherViewManager
import dev.mslalith.focuslauncher.feature.settingspage.R
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsItem
import dev.mslalith.focuslauncher.feature.theme.ThemeSelectionSheet

@Composable
internal fun ChangeTheme() {
    val viewManager = LocalLauncherViewManager.current

    SettingsItem(
        text = stringResource(id = R.string.change_theme),
        onClick = {
            viewManager.showBottomSheet {
                ThemeSelectionSheet(
                    closeBottomSheet = viewManager::hideBottomSheet
                )
            }
        }
    )
}
