package dev.mslalith.focuslauncher.feature.settingspage

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableChooserItem
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableSwitchItem

@Composable
internal fun AppDrawerSettingsSheet(
    settingsPageViewModel: SettingsPageViewModel,
) {
    val appDrawerViewType by settingsPageViewModel.appDrawerViewTypeStateFlow.collectAsState()
    val showAppIcons by settingsPageViewModel.appIconsVisibilityStateFlow.collectAsState()
    val showAppGroupHeader by settingsPageViewModel.appGroupHeaderVisibilityStateFlow.collectAsState()
    val showSearchBar by settingsPageViewModel.searchBarVisibilityStateFlow.collectAsState()

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
                settingsPageViewModel.updateAppDrawerViewType(viewType)
            }
        )
        SettingsSelectableSwitchItem(
            text = "Show Search Bar",
            checked = showSearchBar,
            onClick = { settingsPageViewModel.toggleSearchBarVisibility() }
        )
        SettingsSelectableSwitchItem(
            text = "Group Apps by Character",
            checked = showAppGroupHeader,
            disabled = isViewTypeGrid,
            onClick = { settingsPageViewModel.toggleAppGroupHeaderVisibility() }
        )
        SettingsSelectableSwitchItem(
            text = "Show App Icons",
            checked = showAppIcons,
            disabled = isViewTypeGrid,
            onClick = { settingsPageViewModel.toggleAppIconsVisibility() }
        )
        VerticalSpacer(spacing = 12.dp)
    }
}
