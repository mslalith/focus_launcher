package dev.mslalith.focuslauncher.data.repository

import dev.mslalith.focuslauncher.data.database.dao.AppsDao
import dev.mslalith.focuslauncher.data.database.entities.AppRoom
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppDrawerRepo @Inject constructor(
    private val appsDao: AppsDao,
) {
    val allAppsFlow: Flow<List<AppRoom>>
        get() = appsDao.getAllAppsFlow().map { apps ->
            apps.sortedBy { it.name.lowercase() }
        }

    suspend fun getAppBy(packageName: String): AppRoom? = appsDao.getAppBy(packageName)

    suspend fun addApps(apps: List<AppRoom>) = appsDao.addApps(apps)
    suspend fun addApp(app: AppRoom) = appsDao.addApp(app)
    suspend fun removeApp(app: AppRoom) = appsDao.removeApp(app)

    suspend fun areAppsEmptyInDatabase() = appsDao.getAllApps().isEmpty()
}
