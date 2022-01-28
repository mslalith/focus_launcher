package dev.mslalith.focuslauncher.data.respository.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import dev.mslalith.focuslauncher.utils.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_LUNAR_PHASE
import dev.mslalith.focuslauncher.utils.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_ILLUMINATION_PERCENT
import dev.mslalith.focuslauncher.utils.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_UPCOMING_PHASE_DETAILS
import dev.mslalith.focuslauncher.di.modules.SettingsProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LunarPhaseSettingsRepo @Inject constructor(
    @SettingsProvider private val settingsDataStore: DataStore<Preferences>,
) {

    val showLunarPhaseFlow: Flow<Boolean>
        get() = settingsDataStore.data.map {
            it[PREFERENCES_SHOW_LUNAR_PHASE] ?: DEFAULT_SHOW_LUNAR_PHASE
        }

    val showIlluminationPercentFlow: Flow<Boolean>
        get() = settingsDataStore.data.map {
            it[PREFERENCES_SHOW_ILLUMINATION_PERCENT] ?: DEFAULT_SHOW_ILLUMINATION_PERCENT
        }

    val showUpcomingPhaseDetailsFlow: Flow<Boolean>
        get() = settingsDataStore.data.map {
            it[PREFERENCES_SHOW_UPCOMING_PHASE_DETAILS] ?: DEFAULT_SHOW_UPCOMING_PHASE_DETAILS
        }

    suspend fun toggleShowLunarPhase() = toggleData(
        preference = PREFERENCES_SHOW_LUNAR_PHASE,
        defaultValue = DEFAULT_SHOW_LUNAR_PHASE,
    )

    suspend fun toggleShowIlluminationPercent() = toggleData(
        preference = PREFERENCES_SHOW_ILLUMINATION_PERCENT,
        defaultValue = DEFAULT_SHOW_ILLUMINATION_PERCENT,
    )

    suspend fun toggleShowUpcomingPhaseDetails() = toggleData(
        preference = PREFERENCES_SHOW_UPCOMING_PHASE_DETAILS,
        defaultValue = DEFAULT_SHOW_UPCOMING_PHASE_DETAILS,
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
        private val PREFERENCES_SHOW_LUNAR_PHASE = booleanPreferencesKey("preferences_show_lunar_phase")

        private val PREFERENCES_SHOW_ILLUMINATION_PERCENT =
            booleanPreferencesKey("preferences_show_illumination_percent")

        private val PREFERENCES_SHOW_UPCOMING_PHASE_DETAILS =
            booleanPreferencesKey("preferences_show_upcoming_phase_details")
    }
}
