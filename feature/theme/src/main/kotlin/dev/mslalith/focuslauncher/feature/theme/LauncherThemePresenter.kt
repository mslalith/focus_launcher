package dev.mslalith.focuslauncher.feature.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.presenter.Presenter
import dev.mslalith.focuslauncher.core.domain.theme.GetThemeUseCase
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_THEME
import javax.inject.Inject

class LauncherThemePresenter @Inject constructor(
    private val getThemeUseCase: GetThemeUseCase
) : Presenter<LauncherThemeState> {

    @Composable
    override fun present(): LauncherThemeState {
        val currentTheme by remember { getThemeUseCase() }.collectAsRetainedState(initial = DEFAULT_THEME)

        return LauncherThemeState(
            theme = currentTheme
        )
    }
}
