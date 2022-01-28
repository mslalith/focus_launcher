package dev.mslalith.focuslauncher.data.respository

import dev.mslalith.focuslauncher.data.database.dao.AppsDao
import dev.mslalith.focuslauncher.data.database.dao.FavoriteAppsDao
import dev.mslalith.focuslauncher.data.database.entities.App
import dev.mslalith.focuslauncher.data.database.entities.FavoriteApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoritesRepo @Inject constructor(
    private val appsDao: AppsDao,
    private val favoriteAppsDao: FavoriteAppsDao
) {
    val onlyFavoritesFlow: Flow<List<App>>
        get() = favoriteAppsDao.getFavoriteAppsFlow().map { favorites ->
            favorites.mapNotNull { appsDao.getAppBy(it.packageName) }
        }

    suspend fun addToFavorites(app: App) {
        favoriteAppsDao.addFavorite(FavoriteApp(app.packageName))
    }

    suspend fun removeFromFavorites(packageName: String) {
        favoriteAppsDao.removeFavorite(FavoriteApp(packageName))
    }

    suspend fun clearFavorites() = favoriteAppsDao.clearFavoriteApps()
    suspend fun isFavorite(packageName: String) = favoriteAppsDao.getFavoriteAppBy(packageName) != null
}
