package dev.mslalith.focuslauncher.feature.settingspage.settingsitems

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.mslalith.focuslauncher.core.model.WidgetType
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.testSemantics
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
import dev.mslalith.focuslauncher.feature.settingspage.util.TestTags
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
                viewManager.showBottomSheet {
                    ClockSettingsSheet(
                        modifier = Modifier.testSemantics(tag = TestTags.CLOCK_SETTINGS_SHEET)
                    )
                }
            }
            WidgetType.LUNAR_PHASE -> {
                viewManager.showBottomSheet {
                    LunarPhaseSettingsSheet(
                        modifier = Modifier.testSemantics(tag = TestTags.LUNAR_PHASE_SETTINGS_SHEET),
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
                viewManager.showBottomSheet {
                    QuotesSettingsSheet(
                        modifier = Modifier.testSemantics(tag = TestTags.QUOTE_SETTINGS_SHEET),
                    )
                }
            }
        }
    }

    SettingsExpandableItem(
        modifier = Modifier.testSemantics(tag = TestTags.ITEM_WIDGETS),
        text = stringResource(id = R.string.widgets)
    ) {
        SettingsGridContent(items = widgetValues) { widgetType ->
            SettingsGridItem(
                modifier = Modifier.testSemantics(tag = TestTags.WIDGETS_ITEM) {
                    testWidgetType(widgetType = widgetType)
                },
                text = widgetType.uiText.string(),
                onClick = { onWidgetClick(widgetType) }
            )
        }
    }
}
