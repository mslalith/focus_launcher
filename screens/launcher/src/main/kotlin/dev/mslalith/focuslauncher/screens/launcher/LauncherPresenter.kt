package dev.mslalith.focuslauncher.screens.launcher

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.domain.launcherapps.LoadAllAppsUseCase
import dev.mslalith.focuslauncher.core.screens.LauncherScreen
import dev.mslalith.focuslauncher.feature.appdrawerpage.AppDrawerPagePresenter
import dev.mslalith.focuslauncher.feature.homepage.HomePagePresenter
import dev.mslalith.focuslauncher.feature.settingspage.SettingsPagePresenter
import javax.inject.Inject

@CircuitInject(LauncherScreen::class, SingletonComponent::class)
class LauncherPresenter @Inject constructor(
    private val loadAllAppsUseCase: LoadAllAppsUseCase,
    private val settingsPagePresenter: SettingsPagePresenter,
    private val homePagePresenter: HomePagePresenter,
    private val appDrawerPagePresenter: AppDrawerPagePresenter
) : Presenter<LauncherState> {

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
