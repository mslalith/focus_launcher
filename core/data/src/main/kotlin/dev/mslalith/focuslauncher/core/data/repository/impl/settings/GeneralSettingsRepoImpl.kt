package dev.mslalith.focuslauncher.core.data.repository.impl.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import dev.mslalith.focuslauncher.core.data.di.modules.SettingsProvider
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.data.utils.Constants
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class GeneralSettingsRepoImpl @Inject constructor(
    @SettingsProvider private val settingsDataStore: DataStore<Preferences>
) : GeneralSettingsRepo {
    override val firstRunFlow: Flow<Boolean> = settingsDataStore.data
        .map {
            it[PREFERENCES_FIRST_RUN] ?: Constants.Defaults.Settings.General.DEFAULT_FIRST_RUN
        }

    override val statusBarVisibilityFlow: Flow<Boolean> = settingsDataStore.data
        .map {
            it[PREFERENCES_STATUS_BAR_VISIBILITY] ?: Constants.Defaults.Settings.General.DEFAULT_STATUS_BAR
        }

    override val notificationShadeFlow: Flow<Boolean> = settingsDataStore.data
        .map {
            it[PREFERENCES_NOTIFICATION_SHADE] ?: Constants.Defaults.Settings.General.DEFAULT_NOTIFICATION_SHADE
        }

    override val isDefaultLauncher: Flow<Boolean> = settingsDataStore.data
        .map {
            it[PREFERENCES_IS_DEFAULT_LAUNCHER] ?: Constants.Defaults.Settings.General.DEFAULT_IS_DEFAULT_LAUNCHER
        }

    override suspend fun overrideFirstRun() {
        settingsDataStore.edit { it[PREFERENCES_FIRST_RUN] = false }
    }

    override suspend fun toggleStatusBarVisibility() = toggleData(
        preference = PREFERENCES_STATUS_BAR_VISIBILITY,
        defaultValue = Constants.Defaults.Settings.General.DEFAULT_STATUS_BAR
    )

    override suspend fun toggleNotificationShade() = toggleData(
        preference = PREFERENCES_NOTIFICATION_SHADE,
        defaultValue = Constants.Defaults.Settings.General.DEFAULT_NOTIFICATION_SHADE
    )

    override suspend fun setIsDefaultLauncher(isDefault: Boolean) {
        settingsDataStore.edit {
            it[PREFERENCES_IS_DEFAULT_LAUNCHER] = isDefault
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
        private val PREFERENCES_FIRST_RUN = booleanPreferencesKey("preferences_first_run")

        private val PREFERENCES_STATUS_BAR_VISIBILITY =
            booleanPreferencesKey("preferences_status_bar_visibility")

        private val PREFERENCES_NOTIFICATION_SHADE =
            booleanPreferencesKey("preferences_notification_shade")

        private val PREFERENCES_IS_DEFAULT_LAUNCHER =
            booleanPreferencesKey("preferences_is_default_launcher")
    }
}
