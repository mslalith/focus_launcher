package dev.mslalith.focuslauncher.feature.settingspage.settingsitems

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.mslalith.focuslauncher.feature.settingspage.R
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsItem

@Composable
internal fun PullDownNotifications(
    enableNotificationShade: Boolean,
    onClick: () -> Unit
) {
    val stringRes = when (enableNotificationShade) {
        true -> R.string.disable_pull_down_notifications
        false -> R.string.enable_pull_down_notifications
    }

    SettingsItem(
        text = stringResource(id = stringRes),
        onClick = onClick
    )
}
