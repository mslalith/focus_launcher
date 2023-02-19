package dev.mslalith.focuslauncher.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.mslalith.focuslauncher.core.model.Theme
import dev.mslalith.focuslauncher.data.di.modules.ThemeProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ThemeRepo @Inject constructor(
    @ThemeProvider private val themeDataStore: DataStore<Preferences>
) {
    val currentThemeFlow: Flow<Theme?>
        get() = themeDataStore.data.map {
            val themeName = it[themePreferenceKey] ?: return@map null
            Theme.valueOf(themeName)
        }

    suspend fun changeTheme(theme: Theme) {
        themeDataStore.edit { it[themePreferenceKey] = theme.name }
    }

    companion object {
        val themePreferenceKey = stringPreferencesKey("theme_preference_key")
    }
}
