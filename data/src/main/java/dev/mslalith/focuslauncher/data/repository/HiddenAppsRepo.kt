package dev.mslalith.focuslauncher.data.repository

import dev.mslalith.focuslauncher.data.database.dao.AppsDao
import dev.mslalith.focuslauncher.data.database.dao.HiddenAppsDao
import dev.mslalith.focuslauncher.data.di.modules.AppToRoomMapperProvider
import dev.mslalith.focuslauncher.data.di.modules.HiddenToRoomMapperProvider
import dev.mslalith.focuslauncher.data.dto.AppToRoomMapper
import dev.mslalith.focuslauncher.data.dto.HiddenToRoomMapper
import dev.mslalith.focuslauncher.data.model.App
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HiddenAppsRepo @Inject constructor(
    private val appsDao: AppsDao,
    private val hiddenAppsDao: HiddenAppsDao,
    @AppToRoomMapperProvider private val appToRoomMapper: AppToRoomMapper,
    @HiddenToRoomMapperProvider private val hiddenToRoomMapper: HiddenToRoomMapper
) {
    val onlyHiddenAppsFlow: Flow<List<App>>
        get() = hiddenAppsDao.getHiddenAppsFlow().map { hiddenApps ->
            hiddenApps.mapNotNull {
                val appRoom = appsDao.getAppBy(it.packageName)
                appRoom?.let { it1 -> appToRoomMapper.fromEntity(it1) }
            }
        }

    suspend fun addToHiddenApps(app: App) {
        hiddenAppsDao.hideApp(hiddenToRoomMapper.toEntity(app))
    }

    suspend fun removeFromHiddenApps(packageName: String) {
        val appRoom = appsDao.getAppBy(packageName) ?: throw IllegalStateException("$packageName app was not found in Database")
        val app = appToRoomMapper.fromEntity(appRoom)
        hiddenAppsDao.unHideApp(hiddenToRoomMapper.toEntity(app))
    }

    suspend fun clearHiddenApps() = hiddenAppsDao.clearHiddenApps()

    private suspend fun isHidden(packageName: String) = hiddenAppsDao.getHiddenAppBy(packageName) != null
}
