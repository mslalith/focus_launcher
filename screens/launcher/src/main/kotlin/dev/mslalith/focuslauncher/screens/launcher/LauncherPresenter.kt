package dev.mslalith.focuslauncher.screens.launcher

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.domain.launcherapps.LoadAllAppsUseCase
import dev.mslalith.focuslauncher.core.screens.LauncherScreen
import dev.mslalith.focuslauncher.feature.appdrawerpage.AppDrawerPagePresenter
import dev.mslalith.focuslauncher.feature.homepage.HomePagePresenter
import dev.mslalith.focuslauncher.feature.settingspage.SettingsPagePresenter

class LauncherPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val settingsPagePresenterFactory: SettingsPagePresenter.Factory,
    private val homePagePresenter: HomePagePresenter,
    private val appDrawerPagePresenter: AppDrawerPagePresenter,
    private val loadAllAppsUseCase: LoadAllAppsUseCase
) : Presenter<LauncherState> {

    @CircuitInject(LauncherScreen::class, SingletonComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): LauncherPresenter
    }

    private val settingsPagePresenter by lazy { settingsPagePresenterFactory.create(navigator = navigator) }

    @Composable
    override fun present(): LauncherState {
        val settingsPageState = settingsPagePresenter.present()
        val homePageState = homePagePresenter.present()
        val appDrawerPageState = appDrawerPagePresenter.present()

        LaunchedEffect(key1 = Unit) {
            loadAllAppsUseCase(forceLoad = true)
        }

        return LauncherState(
            settingsPageState = settingsPageState,
            homePageState = homePageState,
            appDrawerPageState = appDrawerPageState
        )
    }
}
