package dev.mslalith.focuslauncher.feature.settingspage.settingsitems

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.mslalith.focuslauncher.core.model.Theme
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

    SettingsExpandableItem(text = "Change Theme") { closeExpandable ->
        SettingsGridContent(items = allThemes) { theme ->
            val isAppliedTheme = currentTheme == theme
            SettingsGridItem(
                text = theme.text,
                showIcon = true,
                iconRes = if (isAppliedTheme) R.drawable.ic_check else null,
                contentDescription = if (isAppliedTheme) "Selected Theme" else theme.text,
                onClick = {
                    if (!isAppliedTheme) changeTheme(theme)
                    closeExpandable()
                }
            )
        }
    }
}
