package dev.mslalith.focuslauncher

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.domain.theme.ChangeThemeUseCase
import dev.mslalith.focuslauncher.core.domain.theme.GetThemeUseCase
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_THEME
import dev.mslalith.focuslauncher.core.model.Theme
import dev.mslalith.focuslauncher.core.ui.extensions.launchInIO
import dev.mslalith.focuslauncher.core.ui.extensions.withinScope
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    getThemeUseCase: GetThemeUseCase,
    private val changeThemeUseCase: ChangeThemeUseCase,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    val currentThemeStateFlow = getThemeUseCase().withinScope(initialValue = DEFAULT_THEME)

    fun changeTheme(theme: Theme) {
        appCoroutineDispatcher.launchInIO {
            changeThemeUseCase(theme = theme)
        }
    }
}
