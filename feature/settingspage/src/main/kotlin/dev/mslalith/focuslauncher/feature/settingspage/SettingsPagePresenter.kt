package dev.mslalith.focuslauncher.feature.settingspage

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.settings.AppDrawerSettingsRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.core.model.BUILD_FLAVOR
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_IS_DEFAULT_LAUNCHER
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_NOTIFICATION_SHADE
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_STATUS_BAR
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerIconViewType
import dev.mslalith.focuslauncher.core.screens.SettingsPageScreen
import dev.mslalith.focuslauncher.feature.settingspage.SettingsPageUiEvent.GoTo
import dev.mslalith.focuslauncher.feature.settingspage.SettingsPageUiEvent.OnBottomSheetOpened
import dev.mslalith.focuslauncher.feature.settingspage.SettingsPageUiEvent.RefreshIsDefaultLauncher
import dev.mslalith.focuslauncher.feature.settingspage.SettingsPageUiEvent.ToggleNotificationShade
import dev.mslalith.focuslauncher.feature.settingspage.SettingsPageUiEvent.ToggleStatusBarVisibility
import dev.mslalith.focuslauncher.feature.settingspage.utils.isDefaultLauncher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SettingsPagePresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val generalSettingsRepo: GeneralSettingsRepo,
    private val appDrawerSettingsRepo: AppDrawerSettingsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : Presenter<SettingsPageState> {

    @CircuitInject(SettingsPageScreen::class, SingletonComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): SettingsPagePresenter
    }

    @Composable
    override fun present(): SettingsPageState {
        val scope = rememberCoroutineScope()

        val showStatusBar by generalSettingsRepo.statusBarVisibilityFlow.collectAsRetainedState(initial = DEFAULT_STATUS_BAR)
        val canDrawNotificationShade by generalSettingsRepo.notificationShadeFlow.collectAsRetainedState(initial = DEFAULT_NOTIFICATION_SHADE)
        val isDefaultLauncher by generalSettingsRepo.isDefaultLauncher.collectAsRetainedState(initial = DEFAULT_IS_DEFAULT_LAUNCHER)

        val appDrawerViewType by appDrawerSettingsRepo.appDrawerViewTypeFlow.collectAsRetainedState(initial = null)
        val appDrawerIconViewType by appDrawerSettingsRepo.appDrawerIconViewTypeFlow.collectAsRetainedState(initial = null)

        val showIconPack by remember {
            derivedStateOf {
                appDrawerViewType == AppDrawerViewType.GRID || appDrawerIconViewType != AppDrawerIconViewType.TEXT
            }
        }

        val showDeveloperOption = BUILD_FLAVOR.isDev()

        return SettingsPageState(
            showStatusBar = showStatusBar,
            canDrawNotificationShade = canDrawNotificationShade,
            showIconPack = showIconPack,
            isDefaultLauncher = isDefaultLauncher,
            showDeveloperOption = showDeveloperOption
        ) {
            when (it) {
                ToggleNotificationShade -> scope.toggleNotificationShade()
                ToggleStatusBarVisibility -> scope.toggleStatusBarVisibility()
                is RefreshIsDefaultLauncher -> scope.refreshIsDefaultLauncher(context = it.context)
                is GoTo -> navigator.goTo(screen = it.screen)
                is OnBottomSheetOpened -> Unit
            }
        }
    }

    private fun CoroutineScope.toggleStatusBarVisibility() {
        launch(appCoroutineDispatcher.io) {
            generalSettingsRepo.toggleStatusBarVisibility()
        }
    }

    private fun CoroutineScope.toggleNotificationShade() {
        launch(appCoroutineDispatcher.io) {
            generalSettingsRepo.toggleNotificationShade()
        }
    }

    private fun CoroutineScope.refreshIsDefaultLauncher(context: Context) {
        launch(appCoroutineDispatcher.io) {
            generalSettingsRepo.setIsDefaultLauncher(isDefault = context.isDefaultLauncher())
        }
    }
}
