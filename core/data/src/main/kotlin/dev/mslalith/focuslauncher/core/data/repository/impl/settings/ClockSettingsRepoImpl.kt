package dev.mslalith.focuslauncher.core.data.repository.impl.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import dev.mslalith.focuslauncher.core.data.di.modules.SettingsProvider
import dev.mslalith.focuslauncher.core.data.repository.settings.ClockSettingsRepo
import dev.mslalith.focuslauncher.core.model.ClockAlignment
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_24_ANIMATION_DURATION
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_ALIGNMENT
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.Clock.DEFAULT_SHOW_CLOCK_24
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.Clock.DEFAULT_USE_24_HOUR
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ClockSettingsRepoImpl @Inject constructor(
    @SettingsProvider private val settingsDataStore: DataStore<Preferences>
) : ClockSettingsRepo {

    override val showClock24Flow: Flow<Boolean> = settingsDataStore.data
        .map {
            it[PREFERENCES_SHOW_CLOCK_24] ?: DEFAULT_SHOW_CLOCK_24
        }

    override val use24HourFlow: Flow<Boolean> = settingsDataStore.data
        .map {
            it[PREFERENCES_USE_24_HOUR] ?: DEFAULT_USE_24_HOUR
        }

    override val clockAlignmentFlow: Flow<ClockAlignment> = settingsDataStore.data
        .map { preferences ->
            val index = preferences[PREFERENCES_CLOCK_ALIGNMENT] ?: DEFAULT_CLOCK_ALIGNMENT.index
            ClockAlignment.values().first { it.index == index }
        }

    override val clock24AnimationDurationFlow: Flow<Int> = settingsDataStore.data
        .map {
            it[PREFERENCES_CLOCK_24_ANIMATION_DURATION] ?: DEFAULT_CLOCK_24_ANIMATION_DURATION
        }

    override suspend fun toggleClock24() {
        settingsDataStore.edit {
            val current = it[PREFERENCES_SHOW_CLOCK_24] ?: DEFAULT_SHOW_CLOCK_24
            it[PREFERENCES_SHOW_CLOCK_24] = !current
        }
    }

    override suspend fun toggleUse24Hour() {
        settingsDataStore.edit {
            val current = it[PREFERENCES_USE_24_HOUR] ?: DEFAULT_USE_24_HOUR
            it[PREFERENCES_USE_24_HOUR] = !current
        }
    }

    override suspend fun updateClockAlignment(clockAlignment: ClockAlignment) {
        settingsDataStore.edit {
            it[PREFERENCES_CLOCK_ALIGNMENT] = clockAlignment.index
        }
    }

    override suspend fun updateClock24AnimationDuration(duration: Int) {
        settingsDataStore.edit {
            it[PREFERENCES_CLOCK_24_ANIMATION_DURATION] = duration
        }
    }

    companion object {
        private val PREFERENCES_SHOW_CLOCK_24 = booleanPreferencesKey(name = "preferences_show_clock_24")
        private val PREFERENCES_USE_24_HOUR = booleanPreferencesKey(name = "preferences_use_24_hour")
        private val PREFERENCES_CLOCK_ALIGNMENT = intPreferencesKey(name = "preferences_clock_alignment")
        private val PREFERENCES_CLOCK_24_ANIMATION_DURATION = intPreferencesKey(name = "preferences_clock_24_animation_duration")
    }
}
