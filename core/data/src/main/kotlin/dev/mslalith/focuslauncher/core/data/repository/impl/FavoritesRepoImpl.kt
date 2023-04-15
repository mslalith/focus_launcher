package dev.mslalith.focuslauncher.core.data.repository.impl

import dev.mslalith.focuslauncher.core.data.database.dao.AppsDao
import dev.mslalith.focuslauncher.core.data.database.dao.FavoriteAppsDao
import dev.mslalith.focuslauncher.core.data.database.entities.AppRoom
import dev.mslalith.focuslauncher.core.data.dto.toApp
import dev.mslalith.focuslauncher.core.data.dto.toFavoriteAppRoom
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.model.App
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first

internal class FavoritesRepoImpl @Inject constructor(
    private val appsDao: AppsDao,
    private val favoriteAppsDao: FavoriteAppsDao,
) : FavoritesRepo {
    override val onlyFavoritesFlow: Flow<List<App>> = favoriteAppsDao.getFavoriteAppsFlow()
        .combine(appsDao.getAllAppsFlow()) { onlyFavorites, allApps ->
            onlyFavorites.filter { favorite ->
                allApps.any { it.packageName == favorite.packageName }
            }.mapNotNull {
                val appRoom = appsDao.getAppBy(it.packageName)
                appRoom?.let(AppRoom::toApp)
            }
        }

    override suspend fun addToFavorites(app: App) {
        favoriteAppsDao.addFavorite(app.toFavoriteAppRoom())
    }

    override suspend fun addToFavorites(apps: List<App>) {
        val favoriteAppRoomList = apps.map(App::toFavoriteAppRoom)
        favoriteAppsDao.addFavorites(favoriteAppRoomList)
    }

    override suspend fun reorderFavorite(app: App, withApp: App) {
        val apps = favoriteAppsDao.getFavoriteAppsFlow().first().toMutableList()
        val appIndex = apps.indexOfFirst { it.packageName == app.packageName }
        val withAppIndex = apps.indexOfFirst { it.packageName == withApp.packageName }
        if (appIndex == -1 || withAppIndex == -1) return

        apps[appIndex] = withApp.toFavoriteAppRoom()
        apps[withAppIndex] = app.toFavoriteAppRoom()

        favoriteAppsDao.clearFavoriteApps()
        favoriteAppsDao.addFavorites(apps)
    }

    override suspend fun removeFromFavorites(packageName: String) {
        val appRoom = appsDao.getAppBy(packageName) ?: error("$packageName app was not found in Database")
        favoriteAppsDao.removeFavorite(appRoom.toFavoriteAppRoom())
    }

    override suspend fun clearFavorites() = favoriteAppsDao.clearFavoriteApps()
    override suspend fun isFavorite(packageName: String) = favoriteAppsDao.getFavoriteAppBy(packageName) != null
}
