package dev.mslalith.focuslauncher.ui.views.bottomsheets.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.R
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.data.models.AppDrawerSettingsProperties
import dev.mslalith.focuslauncher.ui.views.SettingsSelectableChooserItem
import dev.mslalith.focuslauncher.ui.views.SettingsSelectableSwitchItem

@Composable
fun AppDrawerSettingsSheet(
    properties: AppDrawerSettingsProperties
) {
    properties.apply {
        val appDrawerViewType by settingsViewModel.appDrawerViewTypeStateFlow.collectAsState()
        val showAppIcons by settingsViewModel.appIconsVisibilityStateFlow.collectAsState()
        val showAppGroupHeader by settingsViewModel.appGroupHeaderVisibilityStateFlow.collectAsState()
        val showSearchBar by settingsViewModel.searchBarVisibilityStateFlow.collectAsState()

        val isViewTypeGrid by remember {
            derivedStateOf { appDrawerViewType == AppDrawerViewType.GRID }
        }

        val textIconsList = listOf(
            AppDrawerViewType.LIST.text to R.drawable.ic_list,
            AppDrawerViewType.GRID.text to R.drawable.ic_grid
        )

        Column {
            VerticalSpacer(spacing = 24.dp)
            SettingsSelectableChooserItem(
                text = "Apps View Type",
                subText = appDrawerViewType.text,
                textIconsList = textIconsList,
                selectedItem = appDrawerViewType.text,
                onItemSelected = { index ->
                    val viewTypeName = textIconsList[index].first
                    val viewType = AppDrawerViewType.values().first { it.text == viewTypeName }
                    settingsViewModel.updateAppDrawerViewType(viewType)
                }
            )
            SettingsSelectableSwitchItem(
                text = "Show Search Bar",
                checked = showSearchBar,
                onClick = { settingsViewModel.toggleSearchBarVisibility() }
            )
            SettingsSelectableSwitchItem(
                text = "Group Apps by Character",
                checked = showAppGroupHeader,
                disabled = isViewTypeGrid,
                onClick = { settingsViewModel.toggleAppGroupHeaderVisibility() }
            )
            SettingsSelectableSwitchItem(
                text = "Show App Icons",
                checked = showAppIcons,
                disabled = isViewTypeGrid,
                onClick = { settingsViewModel.toggleAppIconsVisibility() }
            )
            VerticalSpacer(spacing = bottomSpacing)
        }
    }
}
