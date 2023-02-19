package dev.mslalith.focuslauncher.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.data.repository.settings.AppDrawerSettingsRepo
import dev.mslalith.focuslauncher.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_DRAWER_VIEW_TYPE
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_GROUP_HEADER
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_ICONS
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.AppDrawer.DEFAULT_SEARCH_BAR
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.General.DEFAULT_FIRST_RUN
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.General.DEFAULT_IS_DEFAULT_LAUNCHER
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.General.DEFAULT_NOTIFICATION_SHADE
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.General.DEFAULT_STATUS_BAR
import dev.mslalith.focuslauncher.extensions.isDefaultLauncher
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val generalSettingsRepo: GeneralSettingsRepo,
    private val appDrawerSettingsRepo: AppDrawerSettingsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    /**
     * General Settings
     */
    val firstRunStateFlow = generalSettingsRepo.firstRunFlow.withinScope(DEFAULT_FIRST_RUN)
    val statusBarVisibilityStateFlow = generalSettingsRepo.statusBarVisibilityFlow.withinScope(DEFAULT_STATUS_BAR)
    val notificationShadeStateFlow = generalSettingsRepo.notificationShadeFlow.withinScope(DEFAULT_NOTIFICATION_SHADE)
    val isDefaultLauncherStateFlow = generalSettingsRepo.isDefaultLauncher.withinScope(DEFAULT_IS_DEFAULT_LAUNCHER)

    fun overrideFirstRun() { launch { generalSettingsRepo.overrideFirstRun() } }
    fun toggleStatusBarVisibility() { launch { generalSettingsRepo.toggleStatusBarVisibility() } }
    fun toggleNotificationShade() { launch { generalSettingsRepo.toggleNotificationShade() } }
    fun refreshIsDefaultLauncher(context: Context) { launch { generalSettingsRepo.setIsDefaultLauncher(context.isDefaultLauncher()) } }

    /**
     * App Drawer Settings
     */
    val appDrawerViewTypeStateFlow = appDrawerSettingsRepo.appDrawerViewTypeFlow.withinScope(DEFAULT_APP_DRAWER_VIEW_TYPE)
    val appIconsVisibilityStateFlow = appDrawerSettingsRepo.appIconsVisibilityFlow.withinScope(DEFAULT_APP_ICONS)
    val searchBarVisibilityStateFlow = appDrawerSettingsRepo.searchBarVisibilityFlow.withinScope(DEFAULT_SEARCH_BAR)
    val appGroupHeaderVisibilityStateFlow = appDrawerSettingsRepo.appGroupHeaderVisibilityFlow.withinScope(DEFAULT_APP_GROUP_HEADER)

    fun updateAppDrawerViewType(appDrawerViewType: AppDrawerViewType) { launch { appDrawerSettingsRepo.updateAppDrawerViewType(appDrawerViewType) } }
    fun toggleAppIconsVisibility() { launch { appDrawerSettingsRepo.toggleAppIconsVisibility() } }
    fun toggleSearchBarVisibility() { launch { appDrawerSettingsRepo.toggleSearchBarVisibility() } }
    fun toggleAppGroupHeaderVisibility() { launch { appDrawerSettingsRepo.toggleAppGroupHeaderVisibility() } }

    private fun launch(
        run: suspend () -> Unit
    ) = viewModelScope.launch(appCoroutineDispatcher.io) { run() }

    private fun <T> Flow<T>.withinScope(
        initialValue: T
    ) = stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = initialValue
    )
}
