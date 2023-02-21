package dev.mslalith.focuslauncher.core.data.repository.impl

import dev.mslalith.focuslauncher.core.data.database.dao.AppsDao
import dev.mslalith.focuslauncher.core.data.database.dao.HiddenAppsDao
import dev.mslalith.focuslauncher.core.data.di.modules.AppToRoomMapperProvider
import dev.mslalith.focuslauncher.core.data.di.modules.HiddenToRoomMapperProvider
import dev.mslalith.focuslauncher.core.data.dto.AppToRoomMapper
import dev.mslalith.focuslauncher.core.data.dto.HiddenToRoomMapper
import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.model.App
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class HiddenAppsRepoImpl @Inject constructor(
    private val appsDao: AppsDao,
    private val hiddenAppsDao: HiddenAppsDao,
    @AppToRoomMapperProvider private val appToRoomMapper: AppToRoomMapper,
    @HiddenToRoomMapperProvider private val hiddenToRoomMapper: HiddenToRoomMapper
) : HiddenAppsRepo {
    override val onlyHiddenAppsFlow: Flow<List<App>>
        get() = hiddenAppsDao.getHiddenAppsFlow().map { hiddenApps ->
            hiddenApps.mapNotNull {
                val appRoom = appsDao.getAppBy(it.packageName)
                appRoom?.let { it1 -> appToRoomMapper.fromEntity(it1) }
            }
        }

    override suspend fun addToHiddenApps(app: App) {
        hiddenAppsDao.hideApp(hiddenToRoomMapper.toEntity(app))
    }

    override suspend fun addToHiddenApps(apps: List<App>) {
        val hiddenAppRoomList = apps.map(hiddenToRoomMapper::toEntity)
        hiddenAppsDao.hideApps(hiddenAppRoomList)
    }

    override suspend fun removeFromHiddenApps(packageName: String) {
        val appRoom = appsDao.getAppBy(packageName) ?: throw IllegalStateException("$packageName app was not found in Database")
        val app = appToRoomMapper.fromEntity(appRoom)
        hiddenAppsDao.unHideApp(hiddenToRoomMapper.toEntity(app))
    }

    override suspend fun clearHiddenApps() = hiddenAppsDao.clearHiddenApps()

    override suspend fun isHidden(packageName: String) = hiddenAppsDao.getHiddenAppBy(packageName) != null
}
