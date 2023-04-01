package dev.mslalith.focuslauncher.core.data.repository.impl

import dev.mslalith.focuslauncher.core.data.database.dao.AppsDao
import dev.mslalith.focuslauncher.core.data.di.modules.AppToRoomMapperProvider
import dev.mslalith.focuslauncher.core.data.dto.AppToRoomMapper
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.model.App
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AppDrawerRepoImpl @Inject constructor(
    private val appsDao: AppsDao,
    @AppToRoomMapperProvider private val appToRoomMapper: AppToRoomMapper
) : AppDrawerRepo {
    override val allAppsFlow: Flow<List<App>>
        get() = appsDao.getAllAppsFlow().map { apps ->
            apps.map(appToRoomMapper::fromEntity).sortedBy { it.name.lowercase() }
        }

    override suspend fun getAppBy(packageName: String): App? {
        val appRoom = appsDao.getAppBy(packageName)
        return appRoom?.let { appToRoomMapper.fromEntity(it) }
    }

    override suspend fun addApps(apps: List<App>) = appsDao.addApps(apps.map(appToRoomMapper::toEntity))
    override suspend fun addApp(app: App) = appsDao.addApp(appToRoomMapper.toEntity(app))
    override suspend fun removeApp(app: App) = appsDao.removeApp(appToRoomMapper.toEntity(app))
    override suspend fun clearApps() = appsDao.clearApps()

    override suspend fun updateDisplayName(app: App, displayName: String) {
        val newApp = app.copy(displayName = displayName)
        appsDao.updateApp(appToRoomMapper.toEntity(newApp))
    }

    override suspend fun areAppsEmptyInDatabase() = appsDao.getAllApps().isEmpty()
}
