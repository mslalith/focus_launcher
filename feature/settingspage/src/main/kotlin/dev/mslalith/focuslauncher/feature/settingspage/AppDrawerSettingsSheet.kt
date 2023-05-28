package dev.mslalith.focuslauncher.feature.settingspage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerIconViewType
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.extensions.string
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableChooserItem
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableSwitchItem
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun AppDrawerSettingsSheet(
    modifier: Modifier = Modifier,
    settingsPageViewModel: SettingsPageViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val appDrawerViewType by settingsPageViewModel.appDrawerViewTypeStateFlow.collectAsStateWithLifecycle()
    val appDrawerIconViewType by settingsPageViewModel.appDrawerIconViewTypeStateFlow.collectAsStateWithLifecycle()
    val showAppGroupHeader by settingsPageViewModel.appGroupHeaderVisibilityStateFlow.collectAsStateWithLifecycle()
    val showSearchBar by settingsPageViewModel.searchBarVisibilityStateFlow.collectAsStateWithLifecycle()

    val isViewTypeGrid by remember {
        derivedStateOf { appDrawerViewType == AppDrawerViewType.GRID }
    }

    val appViewTypeList = remember {
        listOf(
            AppDrawerViewType.LIST.uiText to R.drawable.ic_list,
            AppDrawerViewType.GRID.uiText to R.drawable.ic_grid
        ).map { it.first.string(context = context) to it.second }.toImmutableList()
    }

    val appDrawerIconViewTypeList = remember(key1 = appDrawerViewType) {
        buildList {
            if (appDrawerViewType == AppDrawerViewType.LIST) add(element = AppDrawerIconViewType.TEXT.uiText to R.drawable.ic_app_drawer_text)
            add(element = AppDrawerIconViewType.ICONS.uiText to R.drawable.ic_app_drawer_icons)
            add(element = AppDrawerIconViewType.COLORED.uiText to R.drawable.ic_app_drawer_colored)
        }.map { it.first.string(context = context) to it.second }.toImmutableList()
    }

    Column(
        modifier = modifier
    ) {
        SettingsSelectableChooserItem(
            text = stringResource(id = R.string.apps_view_type),
            subText = appDrawerViewType.uiText.string(),
            textIconsList = appViewTypeList,
            selectedItem = appDrawerViewType.uiText.string(),
            onItemSelected = { index ->
                val viewTypeName = appViewTypeList[index].first
                val viewType = AppDrawerViewType.values().first { it.uiText.string(context = context) == viewTypeName }
                settingsPageViewModel.updateAppDrawerViewType(appDrawerViewType = viewType)
            }
        )
        SettingsSelectableSwitchItem(
            text = stringResource(id = R.string.show_search_bar),
            checked = showSearchBar,
            onClick = settingsPageViewModel::toggleSearchBarVisibility
        )
        SettingsSelectableSwitchItem(
            text = stringResource(id = R.string.group_apps_by_character),
            checked = showAppGroupHeader,
            disabled = isViewTypeGrid,
            onClick = settingsPageViewModel::toggleAppGroupHeaderVisibility
        )
        SettingsSelectableChooserItem(
            text = stringResource(id = R.string.app_icon_type),
            subText = appDrawerIconViewType.uiText.string(),
            textIconsList = appDrawerIconViewTypeList,
            showText = false,
            itemHorizontalArrangement = Arrangement.Center,
            selectedItem = appDrawerIconViewType.uiText.string(),
            onItemSelected = { index ->
                val iconViewTypeName = appDrawerIconViewTypeList[index].first
                val iconViewType = AppDrawerIconViewType.values().first { it.uiText.string(context = context) == iconViewTypeName }
                settingsPageViewModel.updateAppDrawerIconViewType(appDrawerIconViewType = iconViewType)
            }
        )
        VerticalSpacer(spacing = 12.dp)
    }
}
