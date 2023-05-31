package dev.mslalith.focuslauncher.feature.settingspage.settingsitems

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.testSemantics
import dev.mslalith.focuslauncher.feature.settingspage.R
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsItem
import dev.mslalith.focuslauncher.feature.settingspage.utils.TestTags

@Composable
internal fun AppDrawer(
    onClick: () -> Unit
) {
    SettingsItem(
        modifier = Modifier.testSemantics(tag = TestTags.ITEM_APP_DRAWER),
        text = stringResource(id = R.string.app_drawer),
        onClick = onClick
    )
}
