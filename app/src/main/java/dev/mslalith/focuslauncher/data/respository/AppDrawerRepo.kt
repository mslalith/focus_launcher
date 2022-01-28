package dev.mslalith.focuslauncher.data.respository

import dev.mslalith.focuslauncher.data.database.dao.AppsDao
import dev.mslalith.focuslauncher.data.database.entities.App
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppDrawerRepo @Inject constructor(
    private val appsDao: AppsDao,
) {
    val allAppsFlow: Flow<List<App>>
        get() = appsDao.getAllAppsFlow().map { apps ->
            apps.sortedBy { it.name.lowercase() }
        }

    suspend fun getAppBy(packageName: String): App? = appsDao.getAppBy(packageName)

    suspend fun addApps(apps: List<App>) = appsDao.addApps(apps)
    suspend fun addApp(app: App) = appsDao.addApp(app)
    suspend fun removeApp(app: App) = appsDao.removeApp(app)

    suspend fun areAppsEmptyInDatabase() = appsDao.getAllApps().isEmpty()
}
