package dev.mslalith.focuslauncher.data.respository.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import dev.mslalith.focuslauncher.di.modules.SettingsProvider
import dev.mslalith.focuslauncher.utils.Constants.Defaults.Settings.Quotes.DEFAULT_SHOW_QUOTES
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class QuotesSettingsRepo @Inject constructor(
    @SettingsProvider private val settingsDataStore: DataStore<Preferences>,
) {

    val showQuotesFlow: Flow<Boolean>
        get() = settingsDataStore.data.map {
            it[PREFERENCES_SHOW_QUOTES] ?: DEFAULT_SHOW_QUOTES
        }

    suspend fun toggleShowQuotes() = toggleData(
        preference = PREFERENCES_SHOW_QUOTES,
        defaultValue = DEFAULT_SHOW_QUOTES,
    )

    private suspend fun toggleData(
        preference: Preferences.Key<Boolean>,
        defaultValue: Boolean,
    ) {
        settingsDataStore.edit {
            val current = it[preference] ?: defaultValue
            it[preference] = !current
        }
    }

    companion object {
        private val PREFERENCES_SHOW_QUOTES = booleanPreferencesKey("preferences_show_quotes")
    }
}
