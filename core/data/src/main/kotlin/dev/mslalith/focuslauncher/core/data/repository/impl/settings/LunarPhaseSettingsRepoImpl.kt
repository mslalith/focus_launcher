package dev.mslalith.focuslauncher.core.data.repository.impl.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.mslalith.focuslauncher.core.data.di.modules.SettingsProvider
import dev.mslalith.focuslauncher.core.data.repository.settings.LunarPhaseSettingsRepo
import dev.mslalith.focuslauncher.core.data.serializers.CityJsonParser
import dev.mslalith.focuslauncher.core.data.utils.Constants
import dev.mslalith.focuslauncher.core.model.City
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class LunarPhaseSettingsRepoImpl @Inject constructor(
    @SettingsProvider private val settingsDataStore: DataStore<Preferences>,
    private val cityJsonParser: CityJsonParser
) : LunarPhaseSettingsRepo {

    override val showLunarPhaseFlow: Flow<Boolean>
        get() = settingsDataStore.data.map {
            it[PREFERENCES_SHOW_LUNAR_PHASE] ?: Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_LUNAR_PHASE
        }

    override val showIlluminationPercentFlow: Flow<Boolean>
        get() = settingsDataStore.data.map {
            it[PREFERENCES_SHOW_ILLUMINATION_PERCENT] ?: Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_ILLUMINATION_PERCENT
        }

    override val showUpcomingPhaseDetailsFlow: Flow<Boolean>
        get() = settingsDataStore.data.map {
            it[PREFERENCES_SHOW_UPCOMING_PHASE_DETAILS] ?: Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_UPCOMING_PHASE_DETAILS
        }

    override val currentPlaceFlow: Flow<City>
        get() = settingsDataStore.data.map {
            val json = it[PREFERENCES_CURRENT_PLACE] ?: return@map Constants.Defaults.Settings.LunarPhase.DEFAULT_CURRENT_PLACE
            cityJsonParser.fromJson(json)
        }

    override suspend fun toggleShowLunarPhase() = toggleData(
        preference = PREFERENCES_SHOW_LUNAR_PHASE,
        defaultValue = Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_LUNAR_PHASE
    )

    override suspend fun toggleShowIlluminationPercent() = toggleData(
        preference = PREFERENCES_SHOW_ILLUMINATION_PERCENT,
        defaultValue = Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_ILLUMINATION_PERCENT
    )

    override suspend fun toggleShowUpcomingPhaseDetails() = toggleData(
        preference = PREFERENCES_SHOW_UPCOMING_PHASE_DETAILS,
        defaultValue = Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_UPCOMING_PHASE_DETAILS
    )

    override suspend fun updatePlace(city: City) {
        settingsDataStore.edit {
            it[PREFERENCES_CURRENT_PLACE] = cityJsonParser.toJson(city)
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
            stringPreferencesKey("preferences_current_place")
    }
}
