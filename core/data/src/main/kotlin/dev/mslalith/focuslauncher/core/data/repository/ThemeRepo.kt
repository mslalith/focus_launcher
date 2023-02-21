package dev.mslalith.focuslauncher.core.data.repository

import dev.mslalith.focuslauncher.core.model.Theme
import kotlinx.coroutines.flow.Flow

interface ThemeRepo {
    val currentThemeFlow: Flow<Theme?>

    suspend fun changeTheme(theme: Theme)
}
