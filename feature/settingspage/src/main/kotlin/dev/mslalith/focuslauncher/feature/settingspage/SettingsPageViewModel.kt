package dev.mslalith.focuslauncher.feature.settingspage

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.settings.AppDrawerSettingsRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_DRAWER_VIEW_TYPE
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_GROUP_HEADER
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_ICONS
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_SEARCH_BAR
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_IS_DEFAULT_LAUNCHER
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_NOTIFICATION_SHADE
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_STATUS_BAR
import dev.mslalith.focuslauncher.core.ui.extensions.launchInIO
import dev.mslalith.focuslauncher.core.ui.extensions.withinScope
import javax.inject.Inject
import kotlinx.coroutines.flow.combine

@HiltViewModel
internal class SettingsPageViewModel @Inject constructor(
    private val generalSettingsRepo: GeneralSettingsRepo,
    private val appDrawerSettingsRepo: AppDrawerSettingsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    val statusBarVisibilityStateFlow = generalSettingsRepo.statusBarVisibilityFlow.withinScope(initialValue = DEFAULT_STATUS_BAR)
    val notificationShadeStateFlow = generalSettingsRepo.notificationShadeFlow.withinScope(initialValue = DEFAULT_NOTIFICATION_SHADE)
    val isDefaultLauncherStateFlow = generalSettingsRepo.isDefaultLauncher.withinScope(initialValue = DEFAULT_IS_DEFAULT_LAUNCHER)
    val canShowIconPackStateFlow = appDrawerSettingsRepo.appDrawerViewTypeFlow
        .combine(flow = appDrawerSettingsRepo.appIconsVisibilityFlow) { appDrawerViewType, areAppIconsVisible ->
            appDrawerViewType == AppDrawerViewType.GRID || areAppIconsVisible
        }.withinScope(initialValue = true)

    fun toggleStatusBarVisibility() {
        appCoroutineDispatcher.launchInIO {
            generalSettingsRepo.toggleStatusBarVisibility()
        }
    }

    fun toggleNotificationShade() {
        appCoroutineDispatcher.launchInIO {
            generalSettingsRepo.toggleNotificationShade()
        }
    }

    fun refreshIsDefaultLauncher(context: Context) {
        appCoroutineDispatcher.launchInIO {
            generalSettingsRepo.setIsDefaultLauncher(isDefault = context.isDefaultLauncher())
        }
    }

    val appDrawerViewTypeStateFlow = appDrawerSettingsRepo.appDrawerViewTypeFlow.withinScope(initialValue = DEFAULT_APP_DRAWER_VIEW_TYPE)
    val appIconsVisibilityStateFlow = appDrawerSettingsRepo.appIconsVisibilityFlow.withinScope(initialValue = DEFAULT_APP_ICONS)
    val searchBarVisibilityStateFlow = appDrawerSettingsRepo.searchBarVisibilityFlow.withinScope(initialValue = DEFAULT_SEARCH_BAR)
    val appGroupHeaderVisibilityStateFlow = appDrawerSettingsRepo.appGroupHeaderVisibilityFlow.withinScope(initialValue = DEFAULT_APP_GROUP_HEADER)

    fun updateAppDrawerViewType(appDrawerViewType: AppDrawerViewType) {
        appCoroutineDispatcher.launchInIO {
            appDrawerSettingsRepo.updateAppDrawerViewType(appDrawerViewType = appDrawerViewType)
        }
    }

    fun toggleAppIconsVisibility() {
        appCoroutineDispatcher.launchInIO {
            appDrawerSettingsRepo.toggleAppIconsVisibility()
        }
    }

    fun toggleSearchBarVisibility() {
        appCoroutineDispatcher.launchInIO {
            appDrawerSettingsRepo.toggleSearchBarVisibility()
        }
    }

    fun toggleAppGroupHeaderVisibility() {
        appCoroutineDispatcher.launchInIO {
            appDrawerSettingsRepo.toggleAppGroupHeaderVisibility()
        }
    }
}
