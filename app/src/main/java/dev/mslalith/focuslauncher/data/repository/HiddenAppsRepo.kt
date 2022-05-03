package dev.mslalith.focuslauncher.data.repository

import dev.mslalith.focuslauncher.data.database.dao.AppsDao
import dev.mslalith.focuslauncher.data.database.dao.HiddenAppsDao
import dev.mslalith.focuslauncher.data.database.entities.AppRoom
import dev.mslalith.focuslauncher.data.database.entities.HiddenAppRoom
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HiddenAppsRepo @Inject constructor(
    private val appsDao: AppsDao,
    private val hiddenAppsDao: HiddenAppsDao
) {
    val onlyHiddenAppsFlow: Flow<List<AppRoom>>
        get() = hiddenAppsDao.getHiddenAppsFlow().map { hiddenApps ->
            hiddenApps.mapNotNull { appsDao.getAppBy(it.packageName) }
        }

    suspend fun addToHiddenApps(app: AppRoom) {
        hiddenAppsDao.hideApp(HiddenAppRoom(app.packageName))
    }

    suspend fun removeFromHiddenApps(packageName: String) {
        hiddenAppsDao.unHideApp(HiddenAppRoom(packageName))
    }

    suspend fun clearHiddenApps() = hiddenAppsDao.clearHiddenApps()

    private suspend fun isHidden(packageName: String) = hiddenAppsDao.getHiddenAppBy(packageName) != null
}
