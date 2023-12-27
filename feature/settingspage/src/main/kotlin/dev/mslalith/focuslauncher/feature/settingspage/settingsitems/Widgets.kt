package dev.mslalith.focuslauncher.feature.settingspage.settingsitems

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.mslalith.focuslauncher.core.model.WidgetType
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.testSemantics
import dev.mslalith.focuslauncher.core.ui.extensions.string
import dev.mslalith.focuslauncher.feature.settingspage.R
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsExpandableItem
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsGridContent
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsGridItem
import dev.mslalith.focuslauncher.feature.settingspage.utils.TestTags
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun Widgets(
    onClockWidgetClick: () -> Unit,
    onLunarPhaseWidgetClick: () -> Unit,
    onQuotesWidgetClick: () -> Unit
) {
    val widgetValues = remember {
        WidgetType.entries.filter { it != WidgetType.QUOTES }.toImmutableList()
    }

    fun onWidgetClick(widgetType: WidgetType) {
        when (widgetType) {
            WidgetType.CLOCK -> onClockWidgetClick()
            WidgetType.LUNAR_PHASE -> onLunarPhaseWidgetClick()
            WidgetType.QUOTES -> onQuotesWidgetClick()
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
