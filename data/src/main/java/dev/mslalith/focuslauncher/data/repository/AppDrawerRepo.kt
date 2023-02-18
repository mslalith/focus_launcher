package dev.mslalith.focuslauncher.data.repository

import dev.mslalith.focuslauncher.data.database.dao.AppsDao
import dev.mslalith.focuslauncher.data.di.modules.AppToRoomMapperProvider
import dev.mslalith.focuslauncher.data.dto.AppToRoomMapper
import dev.mslalith.focuslauncher.core.model.App
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppDrawerRepo @Inject constructor(
    private val appsDao: AppsDao,
    @AppToRoomMapperProvider private val appToRoomMapper: AppToRoomMapper
) {
    val allAppsFlow: Flow<List<App>>
        get() = appsDao.getAllAppsFlow().map { apps ->
            apps.map(appToRoomMapper::fromEntity).sortedBy { it.name.lowercase() }
        }

    suspend fun getAppBy(packageName: String): App? {
        val appRoom = appsDao.getAppBy(packageName)
        return appRoom?.let { appToRoomMapper.fromEntity(it) }
    }

    suspend fun addApps(apps: List<App>) = appsDao.addApps(apps.map(appToRoomMapper::toEntity))
    suspend fun addApp(app: App) = appsDao.addApp(appToRoomMapper.toEntity(app))
    suspend fun removeApp(app: App) = appsDao.removeApp(appToRoomMapper.toEntity(app))

    suspend fun updateDisplayName(app: App, displayName: String) {
        val newApp = app.copy(displayName = displayName)
        appsDao.updateApp(appToRoomMapper.toEntity(newApp))
    }

    suspend fun areAppsEmptyInDatabase() = appsDao.getAllApps().isEmpty()
}
