package dev.mslalith.focuslauncher.feature.settingspage.bottomsheet.appdrawersettings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.settings.AppDrawerSettingsRepo
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_DRAWER_ICON_VIEW_TYPE
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_DRAWER_VIEW_TYPE
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_GROUP_HEADER
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_SEARCH_BAR
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerIconViewType
import dev.mslalith.focuslauncher.core.screens.AppDrawerSettingsBottomSheetScreen
import dev.mslalith.focuslauncher.feature.settingspage.bottomsheet.appdrawersettings.AppDrawerSettingsBottomSheetUiEvent.ToggleAppGroupHeaderVisibility
import dev.mslalith.focuslauncher.feature.settingspage.bottomsheet.appdrawersettings.AppDrawerSettingsBottomSheetUiEvent.ToggleSearchBarVisibility
import dev.mslalith.focuslauncher.feature.settingspage.bottomsheet.appdrawersettings.AppDrawerSettingsBottomSheetUiEvent.UpdateAppDrawerIconViewType
import dev.mslalith.focuslauncher.feature.settingspage.bottomsheet.appdrawersettings.AppDrawerSettingsBottomSheetUiEvent.UpdateAppDrawerViewType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@CircuitInject(AppDrawerSettingsBottomSheetScreen::class, SingletonComponent::class)
class AppDrawerSettingsBottomSheetPresenter @Inject constructor(
    private val appDrawerSettingsRepo: AppDrawerSettingsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : Presenter<AppDrawerSettingsBottomSheetState> {

    @Composable
    override fun present(): AppDrawerSettingsBottomSheetState {
        val scope = rememberCoroutineScope()

        val appDrawerViewType by appDrawerSettingsRepo.appDrawerViewTypeFlow.collectAsState(initial = DEFAULT_APP_DRAWER_VIEW_TYPE)
        val appDrawerIconViewType by appDrawerSettingsRepo.appDrawerIconViewTypeFlow.collectAsState(initial = DEFAULT_APP_DRAWER_ICON_VIEW_TYPE)
        val showAppGroupHeader by appDrawerSettingsRepo.appGroupHeaderVisibilityFlow.collectAsState(initial = DEFAULT_APP_GROUP_HEADER)
        val showSearchBar by appDrawerSettingsRepo.searchBarVisibilityFlow.collectAsState(initial = DEFAULT_SEARCH_BAR)

        return AppDrawerSettingsBottomSheetState(
            appDrawerViewType = appDrawerViewType,
            appDrawerIconViewType = appDrawerIconViewType,
            showAppGroupHeader = showAppGroupHeader,
            showSearchBar = showSearchBar
        ) {
            when (it) {
                ToggleAppGroupHeaderVisibility -> scope.toggleAppGroupHeaderVisibility()
                ToggleSearchBarVisibility -> scope.toggleSearchBarVisibility()
                is UpdateAppDrawerIconViewType -> scope.updateAppDrawerIconViewType(viewType = it.viewType)
                is UpdateAppDrawerViewType -> scope.updateAppDrawerViewType(viewType = it.viewType)
            }
        }
    }

    private fun CoroutineScope.toggleSearchBarVisibility() {
        launch(appCoroutineDispatcher.io) {
            appDrawerSettingsRepo.toggleSearchBarVisibility()
        }
    }

    private fun CoroutineScope.toggleAppGroupHeaderVisibility() {
        launch(appCoroutineDispatcher.io) {
            appDrawerSettingsRepo.toggleAppGroupHeaderVisibility()
        }
    }

    private fun CoroutineScope.updateAppDrawerViewType(viewType: AppDrawerViewType) {
        launch(appCoroutineDispatcher.io) {
            updateAppDrawerIconViewTypeBasedOnAppDrawerViewType(appDrawerViewType = viewType)
            appDrawerSettingsRepo.updateAppDrawerViewType(appDrawerViewType = viewType)
        }
    }

    private fun CoroutineScope.updateAppDrawerIconViewType(viewType: AppDrawerIconViewType) {
        launch(appCoroutineDispatcher.io) {
            appDrawerSettingsRepo.updateAppDrawerIconViewType(appDrawerIconViewType = viewType)
        }
    }

    private suspend fun updateAppDrawerIconViewTypeBasedOnAppDrawerViewType(appDrawerViewType: AppDrawerViewType) = coroutineScope {
        val appDrawerIconViewType = appDrawerSettingsRepo.appDrawerIconViewTypeFlow.first()
        if (appDrawerViewType == AppDrawerViewType.GRID && appDrawerIconViewType == AppDrawerIconViewType.TEXT) updateAppDrawerIconViewType(viewType = AppDrawerIconViewType.ICONS)
    }
}
