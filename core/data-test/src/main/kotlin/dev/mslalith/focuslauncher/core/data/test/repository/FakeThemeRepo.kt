package dev.mslalith.focuslauncher.core.data.test.repository

import dev.mslalith.focuslauncher.core.data.repository.ThemeRepo
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_THEME
import dev.mslalith.focuslauncher.core.model.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeThemeRepo : ThemeRepo {

    private val currentThemeStateFlow = MutableStateFlow(value = DEFAULT_THEME)
    override val currentThemeFlow: Flow<Theme> = currentThemeStateFlow

    override suspend fun changeTheme(theme: Theme) = currentThemeStateFlow.update { theme }
}
