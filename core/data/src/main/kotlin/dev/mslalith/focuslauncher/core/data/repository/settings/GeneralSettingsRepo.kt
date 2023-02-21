package dev.mslalith.focuslauncher.core.data.repository.settings

import kotlinx.coroutines.flow.Flow

interface GeneralSettingsRepo {
    val firstRunFlow: Flow<Boolean>
    val statusBarVisibilityFlow: Flow<Boolean>
    val notificationShadeFlow: Flow<Boolean>
    val isDefaultLauncher: Flow<Boolean>

    suspend fun overrideFirstRun()
    suspend fun toggleStatusBarVisibility()
    suspend fun toggleNotificationShade()
    suspend fun setIsDefaultLauncher(isDefault: Boolean)
}
