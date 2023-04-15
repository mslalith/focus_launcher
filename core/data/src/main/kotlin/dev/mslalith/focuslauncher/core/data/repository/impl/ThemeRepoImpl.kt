package dev.mslalith.focuslauncher.core.data.repository.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.mslalith.focuslauncher.core.data.di.modules.ThemeProvider
import dev.mslalith.focuslauncher.core.data.repository.ThemeRepo
import dev.mslalith.focuslauncher.core.model.Theme
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ThemeRepoImpl @Inject constructor(
    @ThemeProvider private val themeDataStore: DataStore<Preferences>
) : ThemeRepo {
    override val currentThemeFlow: Flow<Theme?> = themeDataStore.data
        .map {
            val themeName = it[themePreferenceKey] ?: return@map null
            Theme.valueOf(themeName)
        }

    override suspend fun changeTheme(theme: Theme) {
        themeDataStore.edit { it[themePreferenceKey] = theme.name }
    }

    companion object {
        val themePreferenceKey = stringPreferencesKey("theme_preference_key")
    }
}
