package dev.mslalith.focuslauncher.feature.settingspage

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.settings.AppDrawerSettingsRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.data.utils.Constants
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.core.ui.extensions.launchInIO
import dev.mslalith.focuslauncher.core.ui.extensions.withinScope
import javax.inject.Inject

@HiltViewModel
internal class SettingsPageViewModel @Inject constructor(
    private val generalSettingsRepo: GeneralSettingsRepo,
    private val appDrawerSettingsRepo: AppDrawerSettingsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    val statusBarVisibilityStateFlow = generalSettingsRepo.statusBarVisibilityFlow.withinScope(Constants.Defaults.Settings.General.DEFAULT_STATUS_BAR)
    val notificationShadeStateFlow = generalSettingsRepo.notificationShadeFlow.withinScope(Constants.Defaults.Settings.General.DEFAULT_NOTIFICATION_SHADE)
    val isDefaultLauncherStateFlow = generalSettingsRepo.isDefaultLauncher.withinScope(Constants.Defaults.Settings.General.DEFAULT_IS_DEFAULT_LAUNCHER)

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
            generalSettingsRepo.setIsDefaultLauncher(context.isDefaultLauncher())
        }
    }

    val appDrawerViewTypeStateFlow = appDrawerSettingsRepo.appDrawerViewTypeFlow.withinScope(Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_DRAWER_VIEW_TYPE)
    val appIconsVisibilityStateFlow = appDrawerSettingsRepo.appIconsVisibilityFlow.withinScope(Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_ICONS)
    val searchBarVisibilityStateFlow = appDrawerSettingsRepo.searchBarVisibilityFlow.withinScope(Constants.Defaults.Settings.AppDrawer.DEFAULT_SEARCH_BAR)
    val appGroupHeaderVisibilityStateFlow = appDrawerSettingsRepo.appGroupHeaderVisibilityFlow.withinScope(Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_GROUP_HEADER)

    fun updateAppDrawerViewType(appDrawerViewType: AppDrawerViewType) {
        appCoroutineDispatcher.launchInIO {
            appDrawerSettingsRepo.updateAppDrawerViewType(appDrawerViewType)
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
