package dev.mslalith.focuslauncher.feature.theme.bottomsheet.themeselection

import androidx.compose.runtime.Composable
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
import dev.mslalith.focuslauncher.core.domain.theme.ChangeThemeUseCase
import dev.mslalith.focuslauncher.core.domain.theme.GetThemeUseCase
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_THEME
import dev.mslalith.focuslauncher.core.model.Theme
import dev.mslalith.focuslauncher.core.screens.ThemeSelectionBottomSheetScreen
import dev.mslalith.focuslauncher.feature.theme.R
import dev.mslalith.focuslauncher.feature.theme.model.ThemeWithIcon
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThemeSelectionBottomSheetPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val getThemeUseCase: GetThemeUseCase,
    private val changeThemeUseCase: ChangeThemeUseCase,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : Presenter<ThemeSelectionBottomSheetState> {

    @CircuitInject(ThemeSelectionBottomSheetScreen::class, SingletonComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): ThemeSelectionBottomSheetPresenter
    }

    private val allThemes = Theme.entries.map { theme ->
        ThemeWithIcon(
            theme = theme,
            iconRes = when (theme) {
                Theme.FOLLOW_SYSTEM -> R.drawable.ic_device_mobile
                Theme.NOT_WHITE -> R.drawable.ic_sun_dim
                Theme.SAID_DARK -> R.drawable.ic_moon_stars
            }
        )
    }.toImmutableList()

    @Composable
    override fun present(): ThemeSelectionBottomSheetState {
        val scope = rememberCoroutineScope()

        val currentTheme by remember { getThemeUseCase() }.collectAsRetainedState(initial = DEFAULT_THEME)

        return ThemeSelectionBottomSheetState(
            currentTheme = currentTheme,
            allThemes = allThemes
        ) {
            when (it) {
                is ThemeSelectionBottomSheetUiEvent.SelectedTheme -> {
                    if (it.theme != null) scope.changeThemeAndClose(theme = it.theme)
                }
            }
        }
    }

    private fun CoroutineScope.changeThemeAndClose(theme: Theme) {
        launch(appCoroutineDispatcher.io) {
            changeThemeUseCase(theme = theme)
            withContext(appCoroutineDispatcher.main) { navigator.pop() }
        }
    }
}
