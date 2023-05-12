package dev.mslalith.focuslauncher.feature.settingspage.settingsitems

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import dev.mslalith.focuslauncher.core.model.WidgetType
import dev.mslalith.focuslauncher.core.ui.extensions.string
import dev.mslalith.focuslauncher.core.ui.providers.LocalLauncherViewManager
import dev.mslalith.focuslauncher.feature.clock24.settings.ClockSettingsSheet
import dev.mslalith.focuslauncher.feature.lunarcalendar.model.LunarPhaseSettingsProperties
import dev.mslalith.focuslauncher.feature.lunarcalendar.settings.LunarPhaseSettingsSheet
import dev.mslalith.focuslauncher.feature.quoteforyou.settings.QuotesSettingsSheet
import dev.mslalith.focuslauncher.feature.settingspage.R
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsExpandableItem
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsGridContent
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsGridItem
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun Widgets(
    navigateToCurrentPlace: () -> Unit
) {
    val viewManager = LocalLauncherViewManager.current

    val widgetValues = remember {
        WidgetType.values().filter { it != WidgetType.QUOTES }.toImmutableList()
    }

    fun onWidgetClick(widgetType: WidgetType) {
        when (widgetType) {
            WidgetType.CLOCK -> {
                viewManager.showBottomSheet { ClockSettingsSheet() }
            }
            WidgetType.LUNAR_PHASE -> {
                viewManager.showBottomSheet {
                    LunarPhaseSettingsSheet(
                        properties = LunarPhaseSettingsProperties(
                            navigateToCurrentPlace = {
                                viewManager.hideBottomSheet()
                                navigateToCurrentPlace()
                            }
                        )
                    )
                }
            }
            WidgetType.QUOTES -> {
                viewManager.showBottomSheet { QuotesSettingsSheet() }
            }
        }
    }

    SettingsExpandableItem(text = stringResource(id = R.string.widgets)) {
        SettingsGridContent(items = widgetValues) { widgetType ->
            SettingsGridItem(
                text = widgetType.uiText.string(),
                onClick = { onWidgetClick(widgetType) }
            )
        }
    }
}
