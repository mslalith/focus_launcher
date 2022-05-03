package dev.mslalith.focuslauncher.data.repository

import dev.mslalith.focuslauncher.data.database.dao.AppsDao
import dev.mslalith.focuslauncher.data.App
import dev.mslalith.focuslauncher.data.database.entities.AppRoom
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppDrawerRepo @Inject constructor(
    private val appsDao: AppsDao,
) {
    val allAppsFlow: Flow<List<App>>
        get() = appsDao.getAllAppsFlow().map { apps ->
            apps.map { it.toApp() }.sortedBy { it.name.lowercase() }
        }

    suspend fun getAppBy(packageName: String): App? = appsDao.getAppBy(packageName)?.toApp()

    suspend fun addApps(apps: List<App>) = appsDao.addApps(apps.map(AppRoom::fromApp))
    suspend fun addApp(app: App) = appsDao.addApp(AppRoom.fromApp(app))
    suspend fun removeApp(app: App) = appsDao.removeApp(AppRoom.fromApp(app))

    suspend fun areAppsEmptyInDatabase() = appsDao.getAllApps().isEmpty()
}
