package dev.mslalith.focuslauncher.feature.settingspage

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.model.Theme
import dev.mslalith.focuslauncher.core.ui.extensions.launchInIO
import dev.mslalith.focuslauncher.core.ui.extensions.withinScope
import dev.mslalith.focuslauncher.data.repository.ThemeRepo
import dev.mslalith.focuslauncher.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.General.DEFAULT_FIRST_RUN
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeRepo: ThemeRepo,
    generalSettingsRepo: GeneralSettingsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    val firstRunStateFlow = generalSettingsRepo.firstRunFlow.withinScope(DEFAULT_FIRST_RUN)

    val currentThemeStateFlow = themeRepo.currentThemeFlow.withinScope(initialValue = null)

    fun changeTheme(theme: Theme) {
        appCoroutineDispatcher.launchInIO {
            themeRepo.changeTheme(theme)
        }
    }
}
