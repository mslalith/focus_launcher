package dev.mslalith.focuslauncher.core.data.repository.settings

import dev.mslalith.focuslauncher.core.model.IconPackType
import kotlinx.coroutines.flow.Flow

interface GeneralSettingsRepo {
    val firstRunFlow: Flow<Boolean>
    val statusBarVisibilityFlow: Flow<Boolean>
    val notificationShadeFlow: Flow<Boolean>
    val isDefaultLauncher: Flow<Boolean>
    val iconPackTypeFlow: Flow<IconPackType>

    suspend fun overrideFirstRun()
    suspend fun toggleStatusBarVisibility()
    suspend fun toggleNotificationShade()
    suspend fun setIsDefaultLauncher(isDefault: Boolean)
    suspend fun updateIconPackType(iconPackType: IconPackType)
}
