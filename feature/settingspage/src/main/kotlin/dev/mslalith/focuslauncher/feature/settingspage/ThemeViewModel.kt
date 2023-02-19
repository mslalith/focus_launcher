package dev.mslalith.focuslauncher.feature.settingspage

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.model.Theme
import dev.mslalith.focuslauncher.core.ui.extensions.launchInIO
import dev.mslalith.focuslauncher.core.ui.extensions.withinScope
import dev.mslalith.focuslauncher.data.repository.ThemeRepo
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeRepo: ThemeRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    val currentThemeStateFlow = themeRepo.currentThemeFlow.withinScope(initialValue = null)

    fun changeTheme(theme: Theme) {
        appCoroutineDispatcher.launchInIO {
            themeRepo.changeTheme(theme)
        }
    }
}
