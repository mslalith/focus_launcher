package dev.mslalith.focuslauncher.core.data.repository

import dev.mslalith.focuslauncher.core.model.app.App
import kotlinx.coroutines.flow.Flow

interface AppDrawerRepo {
    val allAppsFlow: Flow<List<App>>

    suspend fun getAppBy(packageName: String): App?
    suspend fun addApps(apps: List<App>)
    suspend fun addApp(app: App)
    suspend fun removeApp(app: App)
    suspend fun clearApps()
    suspend fun updateDisplayName(app: App, displayName: String)
    suspend fun areAppsEmptyInDatabase(): Boolean
}
