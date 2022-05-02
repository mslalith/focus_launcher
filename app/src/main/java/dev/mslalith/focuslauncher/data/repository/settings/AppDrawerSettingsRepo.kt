package dev.mslalith.focuslauncher.data.repository.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import dev.mslalith.focuslauncher.data.models.AppDrawerViewType
import dev.mslalith.focuslauncher.di.modules.SettingsProvider
import dev.mslalith.focuslauncher.utils.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_DRAWER_VIEW_TYPE
import dev.mslalith.focuslauncher.utils.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_GROUP_HEADER
import dev.mslalith.focuslauncher.utils.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_ICONS
import dev.mslalith.focuslauncher.utils.Constants.Defaults.Settings.AppDrawer.DEFAULT_SEARCH_BAR
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppDrawerSettingsRepo @Inject constructor(
    @SettingsProvider private val settingsDataStore: DataStore<Preferences>,
) {
    val appDrawerViewTypeFlow: Flow<AppDrawerViewType>
        get() = settingsDataStore.data.map { preferences ->
            val index = preferences[PREFERENCES_APP_DRAWER_VIEW_TYPE] ?: DEFAULT_APP_DRAWER_VIEW_TYPE.index
            AppDrawerViewType.values().first { it.index == index }
        }

    val appIconsVisibilityFlow: Flow<Boolean>
        get() = settingsDataStore.data.map {
            it[PREFERENCES_APP_ICONS_VISIBILITY] ?: DEFAULT_APP_ICONS
        }

    val searchBarVisibilityFlow: Flow<Boolean>
        get() = settingsDataStore.data.map {
            it[PREFERENCES_SEARCH_BAR_VISIBILITY] ?: DEFAULT_SEARCH_BAR
        }

    val appGroupHeaderVisibilityFlow: Flow<Boolean>
        get() = settingsDataStore.data.map {
            it[PREFERENCES_APP_GROUP_HEADER_VISIBILITY] ?: DEFAULT_APP_GROUP_HEADER
        }

    suspend fun updateAppDrawerViewType(appDrawerViewType: AppDrawerViewType) {
        settingsDataStore.edit {
            it[PREFERENCES_APP_DRAWER_VIEW_TYPE] = appDrawerViewType.index
        }
    }

    suspend fun toggleAppIconsVisibility() = toggleData(
        preference = PREFERENCES_APP_ICONS_VISIBILITY,
        defaultValue = DEFAULT_APP_ICONS,
    )

    suspend fun toggleSearchBarVisibility() = toggleData(
        preference = PREFERENCES_SEARCH_BAR_VISIBILITY,
        defaultValue = DEFAULT_SEARCH_BAR,
    )

    suspend fun toggleAppGroupHeaderVisibility() = toggleData(
        preference = PREFERENCES_APP_GROUP_HEADER_VISIBILITY,
        defaultValue = DEFAULT_APP_GROUP_HEADER,
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
        private val PREFERENCES_APP_DRAWER_VIEW_TYPE =
            intPreferencesKey("preferences_app_drawer_view_type")

        private val PREFERENCES_APP_ICONS_VISIBILITY =
            booleanPreferencesKey("preferences_app_icons_visibility")

        private val PREFERENCES_SEARCH_BAR_VISIBILITY =
            booleanPreferencesKey("preferences_search_bar_visibility")

        private val PREFERENCES_APP_GROUP_HEADER_VISIBILITY =
            booleanPreferencesKey("preferences_app_group_header_visibility")
    }
}
