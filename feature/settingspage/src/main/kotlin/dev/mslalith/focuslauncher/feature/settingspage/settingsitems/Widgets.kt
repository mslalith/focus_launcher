package dev.mslalith.focuslauncher.feature.settingspage.settingsitems

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.mslalith.focuslauncher.core.model.WidgetType
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsExpandableItem
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsGridContent
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsGridItem

@Composable
internal fun Widgets(
    onWidgetClick: (WidgetType) -> Unit
) {
    val widgetValues = remember {
        WidgetType.values().filter { it != WidgetType.QUOTES }
    }

    SettingsExpandableItem(text = "Widgets") {
        SettingsGridContent(items = widgetValues) { widgetType ->
            SettingsGridItem(
                text = widgetType.text,
                onClick = { onWidgetClick(widgetType) }
            )
        }
    }
}
