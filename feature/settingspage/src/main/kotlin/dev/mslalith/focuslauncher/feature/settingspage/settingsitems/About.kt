package dev.mslalith.focuslauncher.feature.settingspage.settingsitems

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.mslalith.focuslauncher.feature.settingspage.R
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsItem

@Composable
internal fun About(
    onClick: () -> Unit
) {
    SettingsItem(
        text = stringResource(id = R.string.about),
        onClick = onClick
    )
}
