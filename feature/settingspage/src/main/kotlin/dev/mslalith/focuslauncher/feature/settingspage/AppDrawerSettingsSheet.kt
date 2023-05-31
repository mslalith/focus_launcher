package dev.mslalith.focuslauncher.feature.settingspage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
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
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.testSemantics
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.extensions.string
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableChooserItem
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableSwitchItem
import dev.mslalith.focuslauncher.feature.settingspage.model.SettingsSheetState
import dev.mslalith.focuslauncher.feature.settingspage.utils.TestTags
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun AppDrawerSettingsSheet(
    modifier: Modifier = Modifier,
    settingsPageViewModel: SettingsPageViewModel = hiltViewModel()
) {
    val settingsSheetState by settingsPageViewModel.settingsSheetState.collectAsStateWithLifecycle()

    AppDrawerSettingsSheetInternal(
        modifier = modifier,
        settingsSheetState = settingsSheetState,
        onUpdateAppDrawerViewType = settingsPageViewModel::updateAppDrawerViewType,
        onUpdateAppDrawerIconViewType = settingsPageViewModel::updateAppDrawerIconViewType,
        onToggleSearchBarVisibility = settingsPageViewModel::toggleSearchBarVisibility,
        onToggleAppGroupHeaderVisibility = settingsPageViewModel::toggleAppGroupHeaderVisibility
    )
}

@Composable
internal fun AppDrawerSettingsSheetInternal(
    modifier: Modifier = Modifier,
    settingsSheetState: SettingsSheetState,
    onUpdateAppDrawerViewType: (AppDrawerViewType) -> Unit,
    onUpdateAppDrawerIconViewType: (AppDrawerIconViewType) -> Unit,
    onToggleSearchBarVisibility: () -> Unit,
    onToggleAppGroupHeaderVisibility: () -> Unit
) {
    val context = LocalContext.current

    val isViewTypeGrid = remember(key1 = settingsSheetState.appDrawerViewType) {
        settingsSheetState.appDrawerViewType == AppDrawerViewType.GRID
    }

    val appViewTypeList = remember {
        listOf(
            AppDrawerViewType.LIST.uiText to R.drawable.ic_list,
            AppDrawerViewType.GRID.uiText to R.drawable.ic_grid
        ).map { it.first.string(context = context) to it.second }.toImmutableList()
    }

    val appDrawerIconViewTypeList = remember(key1 = settingsSheetState.appDrawerViewType) {
        buildList {
            if (settingsSheetState.appDrawerViewType == AppDrawerViewType.LIST) add(element = AppDrawerIconViewType.TEXT.uiText to R.drawable.ic_app_drawer_text)
            add(element = AppDrawerIconViewType.ICONS.uiText to R.drawable.ic_app_drawer_icons)
            add(element = AppDrawerIconViewType.COLORED.uiText to R.drawable.ic_app_drawer_colored)
        }.map { it.first.string(context = context) to it.second }.toImmutableList()
    }

    Column(
        modifier = modifier
    ) {
        val appDrawerViewTypeValue = settingsSheetState.appDrawerViewType.uiText.string()
        val appDrawerIconViewTypeValue = settingsSheetState.appDrawerIconViewType.uiText.string()

        SettingsSelectableChooserItem(
            modifier = Modifier.testSemantics(tag = TestTags.SHEET_APPS_VIEW_TYPE) {
                testString(value = appDrawerViewTypeValue)
            },
            chooserGroupModifier = Modifier.testSemantics(tag = TestTags.SHEET_APPS_VIEW_TYPE_CHOOSER_GROUP),
            text = stringResource(id = R.string.apps_view_type),
            subText = appDrawerViewTypeValue,
            textIconsList = appViewTypeList,
            selectedItem = appDrawerViewTypeValue,
            onItemSelected = { index ->
                val viewTypeName = appViewTypeList[index].first
                val viewType = AppDrawerViewType.values().first { it.uiText.string(context = context) == viewTypeName }
                onUpdateAppDrawerViewType(viewType)
            }
        )
        SettingsSelectableSwitchItem(
            modifier = Modifier.testSemantics(tag = TestTags.SHEET_SHOW_SEARCH_BAR) {
                testBoolean(value = settingsSheetState.showSearchBar)
            },
            text = stringResource(id = R.string.show_search_bar),
            checked = settingsSheetState.showSearchBar,
            onClick = onToggleSearchBarVisibility
        )
        SettingsSelectableSwitchItem(
            modifier = Modifier.testSemantics(tag = TestTags.SHEET_SHOW_APP_GROUP_HEADER) {
                testBoolean(value = settingsSheetState.showAppGroupHeader)
            },
            text = stringResource(id = R.string.group_apps_by_character),
            checked = settingsSheetState.showAppGroupHeader,
            disabled = isViewTypeGrid,
            onClick = onToggleAppGroupHeaderVisibility
        )
        SettingsSelectableChooserItem(
            modifier = Modifier.testSemantics(tag = TestTags.SHEET_APP_ICON_TYPE) {
                testString(value = appDrawerIconViewTypeValue)
            },
            chooserGroupModifier = Modifier.testSemantics(tag = TestTags.SHEET_APP_ICON_TYPE_CHOOSER_GROUP),
            text = stringResource(id = R.string.app_icon_type),
            subText = appDrawerIconViewTypeValue,
            textIconsList = appDrawerIconViewTypeList,
            showText = false,
            itemHorizontalArrangement = Arrangement.Center,
            selectedItem = appDrawerIconViewTypeValue,
            onItemSelected = { index ->
                val iconViewTypeName = appDrawerIconViewTypeList[index].first
                val iconViewType = AppDrawerIconViewType.values().first { it.uiText.string(context = context) == iconViewTypeName }
                onUpdateAppDrawerIconViewType(iconViewType)
            }
        )
        VerticalSpacer(spacing = 12.dp)
    }
}
