package dev.mslalith.focuslauncher.core.data.repository.impl

import dev.mslalith.focuslauncher.core.data.database.dao.AppsDao
import dev.mslalith.focuslauncher.core.data.database.dao.HiddenAppsDao
import dev.mslalith.focuslauncher.core.data.database.entities.AppRoom
import dev.mslalith.focuslauncher.core.data.dto.toApp
import dev.mslalith.focuslauncher.core.data.dto.toHiddenAppRoom
import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.model.App
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class HiddenAppsRepoImpl @Inject constructor(
    private val appsDao: AppsDao,
    private val hiddenAppsDao: HiddenAppsDao,
) : HiddenAppsRepo {
    override val onlyHiddenAppsFlow: Flow<List<App>>
        get() = hiddenAppsDao.getHiddenAppsFlow().map { hiddenApps ->
            hiddenApps.mapNotNull {
                val appRoom = appsDao.getAppBy(it.packageName)
                appRoom?.let(AppRoom::toApp)
            }
        }

    override suspend fun addToHiddenApps(app: App) {
        hiddenAppsDao.hideApp(app.toHiddenAppRoom())
    }

    override suspend fun addToHiddenApps(apps: List<App>) {
        val hiddenAppRoomList = apps.map(App::toHiddenAppRoom)
        hiddenAppsDao.hideApps(hiddenAppRoomList)
    }

    override suspend fun removeFromHiddenApps(packageName: String) {
        val appRoom = appsDao.getAppBy(packageName) ?: throw IllegalStateException("$packageName app was not found in Database")
        hiddenAppsDao.unHideApp(appRoom.toHiddenAppRoom())
    }

    override suspend fun clearHiddenApps() = hiddenAppsDao.clearHiddenApps()

    override suspend fun isHidden(packageName: String) = hiddenAppsDao.getHiddenAppBy(packageName) != null
}
