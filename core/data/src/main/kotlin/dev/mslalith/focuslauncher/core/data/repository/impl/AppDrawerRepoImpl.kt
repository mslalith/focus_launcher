package dev.mslalith.focuslauncher.core.data.repository.impl

import dev.mslalith.focuslauncher.core.data.database.dao.AppsDao
import dev.mslalith.focuslauncher.core.data.database.entities.AppRoom
import dev.mslalith.focuslauncher.core.data.dto.toApp
import dev.mslalith.focuslauncher.core.data.dto.toAppRoom
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.model.App
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AppDrawerRepoImpl @Inject constructor(
    private val appsDao: AppsDao
) : AppDrawerRepo {

    override val allAppsFlow: Flow<List<App>> = appsDao.getAllAppsFlow()
        .map { apps ->
            apps.map(AppRoom::toApp).sortedBy { it.name.lowercase() }
        }

    override suspend fun getAppBy(packageName: String): App? {
        val appRoom = appsDao.getAppBy(packageName = packageName)
        return appRoom?.let(AppRoom::toApp)
    }

    override suspend fun addApps(apps: List<App>) = appsDao.addApps(apps = apps.map(App::toAppRoom))
    override suspend fun addApp(app: App) = appsDao.addApp(app = app.toAppRoom())
    override suspend fun removeApp(app: App) = appsDao.removeApp(app = app.toAppRoom())
    override suspend fun clearApps() = appsDao.clearApps()

    override suspend fun updateDisplayName(app: App, displayName: String) {
        val newApp = app.copy(displayName = displayName)
        appsDao.updateApp(app = newApp.toAppRoom())
    }

    override suspend fun areAppsEmptyInDatabase() = appsDao.getAllApps().isEmpty()
}
