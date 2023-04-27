package dev.mslalith.focuslauncher.core.data.repository.impl.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.mslalith.focuslauncher.core.data.di.modules.SettingsProvider
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_FIRST_RUN
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_ICON_PACK_TYPE
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_IS_DEFAULT_LAUNCHER
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_NOTIFICATION_SHADE
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_STATUS_BAR
import dev.mslalith.focuslauncher.core.model.IconPackType
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class GeneralSettingsRepoImpl @Inject constructor(
    @SettingsProvider private val settingsDataStore: DataStore<Preferences>
) : GeneralSettingsRepo {

    override val firstRunFlow: Flow<Boolean> = settingsDataStore.data
        .map {
            it[PREFERENCES_FIRST_RUN] ?: DEFAULT_FIRST_RUN
        }

    override val statusBarVisibilityFlow: Flow<Boolean> = settingsDataStore.data
        .map {
            it[PREFERENCES_STATUS_BAR_VISIBILITY] ?: DEFAULT_STATUS_BAR
        }

    override val notificationShadeFlow: Flow<Boolean> = settingsDataStore.data
        .map {
            it[PREFERENCES_NOTIFICATION_SHADE] ?: DEFAULT_NOTIFICATION_SHADE
        }

    override val isDefaultLauncher: Flow<Boolean> = settingsDataStore.data
        .map {
            it[PREFERENCES_IS_DEFAULT_LAUNCHER] ?: DEFAULT_IS_DEFAULT_LAUNCHER
        }

    override val iconPackTypeFlow: Flow<IconPackType> = settingsDataStore.data
        .map {
            val value = it[PREFERENCES_ICON_PACK_TYPE] ?: DEFAULT_ICON_PACK_TYPE.value
            IconPackType.from(value = value)
        }

    override suspend fun overrideFirstRun() {
        settingsDataStore.edit { it[PREFERENCES_FIRST_RUN] = false }
    }

    override suspend fun toggleStatusBarVisibility() = toggleData(
        preference = PREFERENCES_STATUS_BAR_VISIBILITY,
        defaultValue = DEFAULT_STATUS_BAR
    )

    override suspend fun toggleNotificationShade() = toggleData(
        preference = PREFERENCES_NOTIFICATION_SHADE,
        defaultValue = DEFAULT_NOTIFICATION_SHADE
    )

    override suspend fun setIsDefaultLauncher(isDefault: Boolean) {
        settingsDataStore.edit {
            it[PREFERENCES_IS_DEFAULT_LAUNCHER] = isDefault
        }
    }

    override suspend fun updateIconPackType(iconPackType: IconPackType) {
        settingsDataStore.edit {
            it[PREFERENCES_ICON_PACK_TYPE] = iconPackType.value
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
        private val PREFERENCES_FIRST_RUN = booleanPreferencesKey(name = "preferences_first_run")
        private val PREFERENCES_STATUS_BAR_VISIBILITY = booleanPreferencesKey(name = "preferences_status_bar_visibility")
        private val PREFERENCES_NOTIFICATION_SHADE = booleanPreferencesKey(name = "preferences_notification_shade")
        private val PREFERENCES_IS_DEFAULT_LAUNCHER = booleanPreferencesKey(name = "preferences_is_default_launcher")
        private val PREFERENCES_ICON_PACK_TYPE = stringPreferencesKey(name = "preferences_icon_pack_type")
    }
}
