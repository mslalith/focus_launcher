package dev.mslalith.focuslauncher.core.data.repository

import dev.mslalith.focuslauncher.core.model.app.App
import kotlinx.coroutines.flow.Flow

interface HiddenAppsRepo {
    val onlyHiddenAppsFlow: Flow<List<App>>

    suspend fun addToHiddenApps(app: App)
    suspend fun addToHiddenApps(apps: List<App>)
    suspend fun removeFromHiddenApps(packageName: String)
    suspend fun clearHiddenApps()
    suspend fun isHidden(packageName: String): Boolean
}
