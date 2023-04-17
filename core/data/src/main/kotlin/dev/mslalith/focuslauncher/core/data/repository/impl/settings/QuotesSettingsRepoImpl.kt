package dev.mslalith.focuslauncher.core.data.repository.impl.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import dev.mslalith.focuslauncher.core.data.di.modules.SettingsProvider
import dev.mslalith.focuslauncher.core.data.repository.settings.QuotesSettingsRepo
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.Quotes.DEFAULT_SHOW_QUOTES
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class QuotesSettingsRepoImpl @Inject constructor(
    @SettingsProvider private val settingsDataStore: DataStore<Preferences>
) : QuotesSettingsRepo {

    override val showQuotesFlow: Flow<Boolean> = settingsDataStore.data
        .map {
            it[PREFERENCES_SHOW_QUOTES] ?: DEFAULT_SHOW_QUOTES
        }

    override suspend fun toggleShowQuotes() = toggleData(
        preference = PREFERENCES_SHOW_QUOTES,
        defaultValue = DEFAULT_SHOW_QUOTES
    )

    private suspend fun toggleData(
        preference: Preferences.Key<Boolean>,
        defaultValue: Boolean
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
