package dev.mslalith.focuslauncher.feature.settingspage.settingsitems

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import dev.mslalith.focuslauncher.core.model.Theme
import dev.mslalith.focuslauncher.core.ui.extensions.string
import dev.mslalith.focuslauncher.feature.settingspage.R
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsExpandableItem
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsGridContent
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsGridItem

@Composable
internal fun ChangeTheme(
    currentTheme: Theme?,
    changeTheme: (Theme) -> Unit
) {
    val allThemes = remember { Theme.values().toList() }

    SettingsExpandableItem(text = stringResource(id = R.string.change_theme)) { closeExpandable ->
        SettingsGridContent(items = allThemes) { theme ->
            val isAppliedTheme = currentTheme == theme
            SettingsGridItem(
                text = theme.uiText.string(),
                showIcon = true,
                iconRes = if (isAppliedTheme) R.drawable.ic_check else null,
                contentDescription = if (isAppliedTheme) stringResource(id = R.string.selected_theme) else theme.uiText.string(),
                onClick = {
                    if (!isAppliedTheme) changeTheme(theme)
                    closeExpandable()
                }
            )
        }
    }
}
