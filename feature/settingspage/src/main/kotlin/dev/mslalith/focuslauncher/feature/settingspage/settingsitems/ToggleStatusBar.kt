package dev.mslalith.focuslauncher.feature.settingspage.settingsitems

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.testSemantics
import dev.mslalith.focuslauncher.core.ui.controller.toggleStatusBars
import dev.mslalith.focuslauncher.core.ui.providers.LocalSystemUiController
import dev.mslalith.focuslauncher.feature.settingspage.R
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsItem
import dev.mslalith.focuslauncher.feature.settingspage.utils.TestTags
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
@Composable
internal fun ToggleStatusBar(
    showStatusBar: Boolean,
    onClick: () -> Unit
) {
    val systemUiController = LocalSystemUiController.current

    LaunchedEffect(key1 = showStatusBar) {
        // default value will be emitted for showStatusBar
        // debounce to avoid a flicker when coming back from any screen
        snapshotFlow { showStatusBar }
            .debounce(timeoutMillis = 200)
            .collectLatest {
                systemUiController.toggleStatusBars(show = showStatusBar)
            }
    }

    val stringRes = when (showStatusBar) {
        true -> R.string.hide_status_bar
        false -> R.string.show_status_bar
    }

    SettingsItem(
        modifier = Modifier.testSemantics(tag = TestTags.ITEM_TOGGLE_STATUS_BAR),
        text = stringResource(id = stringRes),
        onClick = onClick
    )
}
