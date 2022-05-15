package dev.mslalith.focuslauncher.data.repository.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import dev.mslalith.focuslauncher.data.di.modules.SettingsProvider
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.General.DEFAULT_FIRST_RUN
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.General.DEFAULT_IS_DEFAULT_LAUNCHER
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.General.DEFAULT_NOTIFICATION_SHADE
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.General.DEFAULT_STATUS_BAR
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GeneralSettingsRepo @Inject constructor(
    @SettingsProvider private val settingsDataStore: DataStore<Preferences>,
) {
    val firstRunFlow: Flow<Boolean>
        get() = settingsDataStore.data.map {
            it[PREFERENCES_FIRST_RUN] ?: DEFAULT_FIRST_RUN
        }

    val statusBarVisibilityFlow: Flow<Boolean>
        get() = settingsDataStore.data.map {
            it[PREFERENCES_STATUS_BAR_VISIBILITY] ?: DEFAULT_STATUS_BAR
        }

    val notificationShadeFlow: Flow<Boolean>
        get() = settingsDataStore.data.map {
            it[PREFERENCES_NOTIFICATION_SHADE] ?: DEFAULT_NOTIFICATION_SHADE
        }

    val isDefaultLauncher: Flow<Boolean>
        get() = settingsDataStore.data.map {
            it[PREFERENCES_IS_DEFAULT_LAUNCHER] ?: DEFAULT_IS_DEFAULT_LAUNCHER
        }

    suspend fun overrideFirstRun() {
        settingsDataStore.edit { it[PREFERENCES_FIRST_RUN] = false }
    }

    suspend fun toggleStatusBarVisibility() = toggleData(
        preference = PREFERENCES_STATUS_BAR_VISIBILITY,
        defaultValue = DEFAULT_STATUS_BAR,
    )

    suspend fun toggleNotificationShade() = toggleData(
        preference = PREFERENCES_NOTIFICATION_SHADE,
        defaultValue = DEFAULT_NOTIFICATION_SHADE,
    )

    suspend fun setIsDefaultLauncher(isDefault: Boolean) {
        settingsDataStore.edit {
            it[PREFERENCES_IS_DEFAULT_LAUNCHER] = isDefault
        }
    }

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
        private val PREFERENCES_FIRST_RUN = booleanPreferencesKey("preferences_first_run")

        private val PREFERENCES_STATUS_BAR_VISIBILITY =
            booleanPreferencesKey("preferences_status_bar_visibility")

        private val PREFERENCES_NOTIFICATION_SHADE =
            booleanPreferencesKey("preferences_notification_shade")

        private val PREFERENCES_IS_DEFAULT_LAUNCHER =
            booleanPreferencesKey("preferences_is_default_launcher")
    }
}
