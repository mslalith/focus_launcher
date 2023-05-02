package dev.mslalith.focuslauncher.feature.settingspage.settingsitems

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import dev.mslalith.focuslauncher.core.model.WidgetType
import dev.mslalith.focuslauncher.core.ui.extensions.string
import dev.mslalith.focuslauncher.feature.settingspage.R
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

    SettingsExpandableItem(text = stringResource(id = R.string.widgets)) {
        SettingsGridContent(items = widgetValues) { widgetType ->
            SettingsGridItem(
                text = widgetType.uiText.string(),
                onClick = { onWidgetClick(widgetType) }
            )
        }
    }
}
