package dev.mslalith.focuslauncher.core.data.repository.settings

import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import kotlinx.coroutines.flow.Flow

interface AppDrawerSettingsRepo {
    val appDrawerViewTypeFlow: Flow<AppDrawerViewType>
    val appIconsVisibilityFlow: Flow<Boolean>
    val searchBarVisibilityFlow: Flow<Boolean>
    val appGroupHeaderVisibilityFlow: Flow<Boolean>

    suspend fun updateAppDrawerViewType(appDrawerViewType: AppDrawerViewType)
    suspend fun toggleAppIconsVisibility()
    suspend fun toggleSearchBarVisibility()
    suspend fun toggleAppGroupHeaderVisibility()
}
