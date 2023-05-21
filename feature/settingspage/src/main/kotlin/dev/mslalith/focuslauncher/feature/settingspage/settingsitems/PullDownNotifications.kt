package dev.mslalith.focuslauncher.feature.settingspage.settingsitems

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.testSemantics
import dev.mslalith.focuslauncher.feature.settingspage.R
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsItem
import dev.mslalith.focuslauncher.feature.settingspage.utils.TestTags

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
        modifier = Modifier.testSemantics(tag = TestTags.ITEM_TOGGLE_PULL_DOWN_NOTIFICATION),
        text = stringResource(id = stringRes),
        onClick = onClick
    )
}
