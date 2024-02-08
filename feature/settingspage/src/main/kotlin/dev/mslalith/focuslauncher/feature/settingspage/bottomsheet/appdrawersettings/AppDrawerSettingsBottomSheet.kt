package dev.mslalith.focuslauncher.feature.settingspage.bottomsheet.appdrawersettings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerIconViewType
import dev.mslalith.focuslauncher.core.screens.AppDrawerSettingsBottomSheetScreen
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.testSemantics
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.extensions.string
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableChooserItem
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableSwitchItem
import dev.mslalith.focuslauncher.feature.settingspage.R
import dev.mslalith.focuslauncher.feature.settingspage.utils.TestTags
import kotlinx.collections.immutable.toImmutableList

@CircuitInject(AppDrawerSettingsBottomSheetScreen::class, SingletonComponent::class)
@Composable
fun AppDrawerSettingsBottomSheet(
    state: AppDrawerSettingsBottomSheetState,
    modifier: Modifier = Modifier
) {
    // Need to extract the eventSink out to a local val, so that the Compose Compiler
    // treats it as stable. See: https://issuetracker.google.com/issues/256100927
    val eventSink = state.eventSink

    AppDrawerSettingsBottomSheet(
        modifier = modifier,
        sheetState = state,
        onUpdateAppDrawerViewType = { eventSink(AppDrawerSettingsBottomSheetUiEvent.UpdateAppDrawerViewType(viewType = it)) },
        onUpdateAppDrawerIconViewType = { eventSink(AppDrawerSettingsBottomSheetUiEvent.UpdateAppDrawerIconViewType(viewType = it)) },
        onToggleSearchBarVisibility = { eventSink(AppDrawerSettingsBottomSheetUiEvent.ToggleSearchBarVisibility) },
        onToggleAppGroupHeaderVisibility = { eventSink(AppDrawerSettingsBottomSheetUiEvent.ToggleAppGroupHeaderVisibility) }
    )
}

@Composable
private fun AppDrawerSettingsBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: AppDrawerSettingsBottomSheetState,
    onUpdateAppDrawerViewType: (AppDrawerViewType) -> Unit,
    onUpdateAppDrawerIconViewType: (AppDrawerIconViewType) -> Unit,
    onToggleSearchBarVisibility: () -> Unit,
    onToggleAppGroupHeaderVisibility: () -> Unit
) {
    val context = LocalContext.current

    val isViewTypeGrid = remember(key1 = sheetState.appDrawerViewType) {
        sheetState.appDrawerViewType == AppDrawerViewType.GRID
    }

    val appViewTypeList = remember {
        listOf(
            AppDrawerViewType.LIST.uiText to R.drawable.ic_list,
            AppDrawerViewType.GRID.uiText to R.drawable.ic_grid
        ).map { it.first.string(context = context) to it.second }.toImmutableList()
    }

    val appDrawerIconViewTypeList = remember(key1 = sheetState.appDrawerViewType) {
        buildList {
            if (sheetState.appDrawerViewType == AppDrawerViewType.LIST) add(element = AppDrawerIconViewType.TEXT.uiText to R.drawable.ic_app_drawer_text)
            add(element = AppDrawerIconViewType.ICONS.uiText to R.drawable.ic_app_drawer_icons)
            add(element = AppDrawerIconViewType.COLORED.uiText to R.drawable.ic_app_drawer_colored)
        }.map { it.first.string(context = context) to it.second }.toImmutableList()
    }

    Column(
        modifier = modifier
    ) {
        val appDrawerViewTypeValue = sheetState.appDrawerViewType.uiText.string()
        val appDrawerIconViewTypeValue = sheetState.appDrawerIconViewType.uiText.string()

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
                val viewType = AppDrawerViewType.entries.first { it.uiText.string(context = context) == viewTypeName }
                onUpdateAppDrawerViewType(viewType)
            }
        )
        SettingsSelectableSwitchItem(
            modifier = Modifier.testSemantics(tag = TestTags.SHEET_SHOW_SEARCH_BAR) {
                testBoolean(value = sheetState.showSearchBar)
            },
            text = stringResource(id = R.string.show_search_bar),
            checked = sheetState.showSearchBar,
            onClick = onToggleSearchBarVisibility
        )
        SettingsSelectableSwitchItem(
            modifier = Modifier.testSemantics(tag = TestTags.SHEET_SHOW_APP_GROUP_HEADER) {
                testBoolean(value = sheetState.showAppGroupHeader)
            },
            text = stringResource(id = R.string.group_apps_by_character),
            checked = sheetState.showAppGroupHeader,
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
                val iconViewType = AppDrawerIconViewType.entries.first { it.uiText.string(context = context) == iconViewTypeName }
                onUpdateAppDrawerIconViewType(iconViewType)
            }
        )
        VerticalSpacer(spacing = 12.dp)
    }
}
