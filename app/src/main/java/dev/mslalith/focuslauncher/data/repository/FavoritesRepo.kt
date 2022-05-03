package dev.mslalith.focuslauncher.data.repository

import dev.mslalith.focuslauncher.data.App
import dev.mslalith.focuslauncher.data.database.dao.AppsDao
import dev.mslalith.focuslauncher.data.database.dao.FavoriteAppsDao
import dev.mslalith.focuslauncher.data.database.entities.FavoriteAppRoom
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoritesRepo @Inject constructor(
    private val appsDao: AppsDao,
    private val favoriteAppsDao: FavoriteAppsDao
) {
    val onlyFavoritesFlow: Flow<List<App>>
        get() = favoriteAppsDao.getFavoriteAppsFlow().map { favorites ->
            favorites.mapNotNull { appsDao.getAppBy(it.packageName)?.toApp() }
        }

    suspend fun addToFavorites(app: App) {
        favoriteAppsDao.addFavorite(FavoriteAppRoom(app.packageName))
    }

    suspend fun reorderFavorite(app: App, withApp: App) {
        val apps = favoriteAppsDao.getFavoriteAppsFlow().first().toMutableList()
        val appIndex = apps.indexOfFirst { it.packageName == app.packageName }
        val withAppIndex = apps.indexOfFirst { it.packageName == withApp.packageName }
        if (appIndex == -1 || withAppIndex == -1) return

        apps[appIndex] = FavoriteAppRoom(app.packageName)
        apps[withAppIndex] = FavoriteAppRoom(withApp.packageName)

        favoriteAppsDao.clearFavoriteApps()
        favoriteAppsDao.addFavorites(apps)
    }

    suspend fun removeFromFavorites(packageName: String) {
        favoriteAppsDao.removeFavorite(FavoriteAppRoom(packageName))
    }

    suspend fun clearFavorites() = favoriteAppsDao.clearFavoriteApps()
    suspend fun isFavorite(packageName: String) = favoriteAppsDao.getFavoriteAppBy(packageName) != null
}
