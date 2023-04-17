package dev.mslalith.focuslauncher.core.data.repository.impl.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.mslalith.focuslauncher.core.data.di.modules.SettingsProvider
import dev.mslalith.focuslauncher.core.data.repository.settings.LunarPhaseSettingsRepo
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.LunarPhase.DEFAULT_CURRENT_PLACE
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_ILLUMINATION_PERCENT
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_LUNAR_PHASE
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_UPCOMING_PHASE_DETAILS
import dev.mslalith.focuslauncher.core.model.CurrentPlace
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

internal class LunarPhaseSettingsRepoImpl @Inject constructor(
    @SettingsProvider private val settingsDataStore: DataStore<Preferences>,
) : LunarPhaseSettingsRepo {

    override val showLunarPhaseFlow: Flow<Boolean> = settingsDataStore.data
        .map {
            it[PREFERENCES_SHOW_LUNAR_PHASE] ?: DEFAULT_SHOW_LUNAR_PHASE
        }

    override val showIlluminationPercentFlow: Flow<Boolean> = settingsDataStore.data
        .map {
            it[PREFERENCES_SHOW_ILLUMINATION_PERCENT] ?: DEFAULT_SHOW_ILLUMINATION_PERCENT
        }

    override val showUpcomingPhaseDetailsFlow: Flow<Boolean> = settingsDataStore.data
        .map {
            it[PREFERENCES_SHOW_UPCOMING_PHASE_DETAILS] ?: DEFAULT_SHOW_UPCOMING_PHASE_DETAILS
        }

    override val currentPlaceFlow: Flow<CurrentPlace> = settingsDataStore.data
        .map {
            val json = it[PREFERENCES_CURRENT_PLACE] ?: return@map DEFAULT_CURRENT_PLACE
            Json.decodeFromString(deserializer = CurrentPlace.serializer(), string = json)
        }

    override suspend fun toggleShowLunarPhase() = toggleData(
        preference = PREFERENCES_SHOW_LUNAR_PHASE,
        defaultValue = DEFAULT_SHOW_LUNAR_PHASE
    )

    override suspend fun toggleShowIlluminationPercent() = toggleData(
        preference = PREFERENCES_SHOW_ILLUMINATION_PERCENT,
        defaultValue = DEFAULT_SHOW_ILLUMINATION_PERCENT
    )

    override suspend fun toggleShowUpcomingPhaseDetails() = toggleData(
        preference = PREFERENCES_SHOW_UPCOMING_PHASE_DETAILS,
        defaultValue = DEFAULT_SHOW_UPCOMING_PHASE_DETAILS
    )

    override suspend fun updateCurrentPlace(currentPlace: CurrentPlace) {
        settingsDataStore.edit {
            it[PREFERENCES_CURRENT_PLACE] = Json.encodeToString(serializer = CurrentPlace.serializer(), value = currentPlace)
        }
    }

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
        private val PREFERENCES_SHOW_LUNAR_PHASE = booleanPreferencesKey("preferences_show_lunar_phase")

        private val PREFERENCES_SHOW_ILLUMINATION_PERCENT =
            booleanPreferencesKey("preferences_show_illumination_percent")

        private val PREFERENCES_SHOW_UPCOMING_PHASE_DETAILS =
            booleanPreferencesKey("preferences_show_upcoming_phase_details")

        private val PREFERENCES_CURRENT_PLACE =
            stringPreferencesKey("preferences_current_place_key")
    }
}
