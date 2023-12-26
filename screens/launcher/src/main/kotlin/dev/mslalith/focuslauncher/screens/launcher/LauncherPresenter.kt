package dev.mslalith.focuslauncher.screens.launcher

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.domain.launcherapps.LoadAllAppsUseCase
import dev.mslalith.focuslauncher.core.screens.LauncherScreen
import javax.inject.Inject

@CircuitInject(LauncherScreen::class, SingletonComponent::class)
class LauncherPresenter @Inject constructor(
    private val loadAllAppsUseCase: LoadAllAppsUseCase
) : Presenter<LauncherState> {

    @Composable
    override fun present(): LauncherState {

        LaunchedEffect(key1 = Unit) {
            loadAllAppsUseCase(forceLoad = true)
        }

        return LauncherState
    }
}
