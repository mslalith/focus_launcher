package dev.mslalith.focuslauncher.core.data.test.repository.settings

import dev.mslalith.focuslauncher.core.data.repository.settings.AppDrawerSettingsRepo
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_DRAWER_ICON_VIEW_TYPE
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_DRAWER_VIEW_TYPE
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_GROUP_HEADER
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_ICONS
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_SEARCH_BAR
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerIconViewType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet

class FakeAppDrawerSettingsRepo : AppDrawerSettingsRepo {

    private val appDrawerViewTypeStateFlow = MutableStateFlow(value = DEFAULT_APP_DRAWER_VIEW_TYPE)
    override val appDrawerViewTypeFlow: Flow<AppDrawerViewType> = appDrawerViewTypeStateFlow

    private val appIconsVisibilityStateFlow = MutableStateFlow(value = DEFAULT_APP_ICONS)
    override val appIconsVisibilityFlow: Flow<Boolean> = appIconsVisibilityStateFlow

    private val appDrawerIconViewTypeStateFlow = MutableStateFlow(value = DEFAULT_APP_DRAWER_ICON_VIEW_TYPE)
    override val appDrawerIconViewTypeFlow: Flow<AppDrawerIconViewType> = appDrawerIconViewTypeStateFlow

    private val searchBarVisibilityStateFlow = MutableStateFlow(value = DEFAULT_SEARCH_BAR)
    override val searchBarVisibilityFlow: Flow<Boolean> = searchBarVisibilityStateFlow

    private val appGroupHeaderVisibilityStateFlow= MutableStateFlow(value = DEFAULT_APP_GROUP_HEADER)
    override val appGroupHeaderVisibilityFlow: Flow<Boolean> = appGroupHeaderVisibilityStateFlow

    override suspend fun updateAppDrawerViewType(appDrawerViewType: AppDrawerViewType) {
        appDrawerViewTypeStateFlow.update { appDrawerViewType }
    }

    override suspend fun updateAppDrawerIconViewType(appDrawerIconViewType: AppDrawerIconViewType) {
        appDrawerIconViewTypeStateFlow.update { appDrawerIconViewType }
    }

    override suspend fun toggleAppIconsVisibility() {
        appIconsVisibilityStateFlow.update { !it }
    }

    override suspend fun toggleSearchBarVisibility() {
        searchBarVisibilityStateFlow.update { !it }
    }

    override suspend fun toggleAppGroupHeaderVisibility() {
        appGroupHeaderVisibilityStateFlow.updateAndGet { !it }
    }
}
