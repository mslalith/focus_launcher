package dev.mslalith.focuslauncher.core.data.repository.impl.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import dev.mslalith.focuslauncher.core.data.di.modules.SettingsProvider
import dev.mslalith.focuslauncher.core.data.repository.settings.AppDrawerSettingsRepo
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_DRAWER_VIEW_TYPE
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_GROUP_HEADER
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_ICONS
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.AppDrawer.DEFAULT_SEARCH_BAR
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AppDrawerSettingsRepoImpl @Inject constructor(
    @SettingsProvider private val settingsDataStore: DataStore<Preferences>
) : AppDrawerSettingsRepo {

    override val appDrawerViewTypeFlow: Flow<AppDrawerViewType> = settingsDataStore.data
        .map { preferences ->
            val index = preferences[PREFERENCES_APP_DRAWER_VIEW_TYPE] ?: DEFAULT_APP_DRAWER_VIEW_TYPE.index
            AppDrawerViewType.values().first { it.index == index }
        }

    override val appIconsVisibilityFlow: Flow<Boolean> = settingsDataStore.data
        .map {
            it[PREFERENCES_APP_ICONS_VISIBILITY] ?: DEFAULT_APP_ICONS
        }

    override val searchBarVisibilityFlow: Flow<Boolean> = settingsDataStore.data
        .map {
            it[PREFERENCES_SEARCH_BAR_VISIBILITY] ?: DEFAULT_SEARCH_BAR
        }

    override val appGroupHeaderVisibilityFlow: Flow<Boolean> = settingsDataStore.data
        .map {
            it[PREFERENCES_APP_GROUP_HEADER_VISIBILITY] ?: DEFAULT_APP_GROUP_HEADER
        }

    override suspend fun updateAppDrawerViewType(appDrawerViewType: AppDrawerViewType) {
        settingsDataStore.edit {
            it[PREFERENCES_APP_DRAWER_VIEW_TYPE] = appDrawerViewType.index
        }
    }

    override suspend fun toggleAppIconsVisibility() = toggleData(
        preference = PREFERENCES_APP_ICONS_VISIBILITY,
        defaultValue = DEFAULT_APP_ICONS
    )

    override suspend fun toggleSearchBarVisibility() = toggleData(
        preference = PREFERENCES_SEARCH_BAR_VISIBILITY,
        defaultValue = DEFAULT_SEARCH_BAR
    )

    override suspend fun toggleAppGroupHeaderVisibility() = toggleData(
        preference = PREFERENCES_APP_GROUP_HEADER_VISIBILITY,
        defaultValue = DEFAULT_APP_GROUP_HEADER
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
        private val PREFERENCES_APP_DRAWER_VIEW_TYPE = intPreferencesKey(name = "preferences_app_drawer_view_type")
        private val PREFERENCES_APP_ICONS_VISIBILITY = booleanPreferencesKey(name = "preferences_app_icons_visibility")
        private val PREFERENCES_SEARCH_BAR_VISIBILITY = booleanPreferencesKey(name = "preferences_search_bar_visibility")
        private val PREFERENCES_APP_GROUP_HEADER_VISIBILITY = booleanPreferencesKey(name = "preferences_app_group_header_visibility")
    }
}
