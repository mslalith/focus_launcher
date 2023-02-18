package dev.mslalith.focuslauncher.ui.views.bottomsheets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.data.models.BottomSheetContentType
import dev.mslalith.focuslauncher.data.models.BottomSheetContentType.MoreAppOptions
import dev.mslalith.focuslauncher.data.models.BottomSheetContentType.Widgets.Clock
import dev.mslalith.focuslauncher.data.models.BottomSheetContentType.Widgets.LunarPhase
import dev.mslalith.focuslauncher.data.models.BottomSheetContentType.Widgets.Quotes
import dev.mslalith.focuslauncher.data.providers.LocalLauncherViewManager
import dev.mslalith.focuslauncher.extensions.VerticalSpacer
import dev.mslalith.focuslauncher.ui.views.bottomsheets.appdrawer.MoreOptionsBottomSheet
import dev.mslalith.focuslauncher.ui.views.bottomsheets.settings.AppDrawerSettingsSheet
import dev.mslalith.focuslauncher.ui.views.bottomsheets.settings.ClockSettingsSheet
import dev.mslalith.focuslauncher.ui.views.bottomsheets.settings.LunarPhaseSettingsSheet
import dev.mslalith.focuslauncher.ui.views.bottomsheets.settings.QuotesSettingsSheet

@Composable
fun LauncherBottomSheetContent() {
    val viewManager = LocalLauncherViewManager.current
    val bottomSheetContentType by viewManager.sheetContentTypeStateFlow.collectAsState()

    Box(
        modifier = Modifier.navigationBarsPadding()
    ) {
        bottomSheetContentType.run {
            when (this) {
                is BottomSheetContentType.AppDrawer -> AppDrawerSettingsSheet(properties = properties)
                is MoreAppOptions -> MoreOptionsBottomSheet(properties = properties)
                is Clock -> ClockSettingsSheet(properties = properties)
                is LunarPhase -> LunarPhaseSettingsSheet(properties = properties)
                is Quotes -> QuotesSettingsSheet(properties = properties)
                null -> VerticalSpacer(spacing = 0.5.dp)
            }
        }
    }
}
