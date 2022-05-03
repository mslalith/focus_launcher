package dev.mslalith.focuslauncher.data.repository

import dev.mslalith.focuslauncher.data.database.dao.AppsDao
import dev.mslalith.focuslauncher.data.database.dao.FavoriteAppsDao
import dev.mslalith.focuslauncher.data.di.modules.AppToRoomMapperProvider
import dev.mslalith.focuslauncher.data.di.modules.FavoriteToRoomMapperProvider
import dev.mslalith.focuslauncher.data.dto.AppToRoomMapper
import dev.mslalith.focuslauncher.data.dto.FavoriteToRoomMapper
import dev.mslalith.focuslauncher.data.model.App
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoritesRepo @Inject constructor(
    private val appsDao: AppsDao,
    private val favoriteAppsDao: FavoriteAppsDao,
    @AppToRoomMapperProvider private val appToRoomMapper: AppToRoomMapper,
    @FavoriteToRoomMapperProvider private val favoriteToRoomMapper: FavoriteToRoomMapper
) {
    val onlyFavoritesFlow: Flow<List<App>>
        get() = favoriteAppsDao.getFavoriteAppsFlow().map { favorites ->
            favorites.mapNotNull {
                val appRoom = appsDao.getAppBy(it.packageName)
                appRoom?.let { it1 -> appToRoomMapper.fromEntity(it1) }
            }
        }

    suspend fun addToFavorites(app: App) {
        val favoriteAppRoom = favoriteToRoomMapper.toEntity(app)
        favoriteAppsDao.addFavorite(favoriteAppRoom)
    }

    suspend fun reorderFavorite(app: App, withApp: App) {
        val apps = favoriteAppsDao.getFavoriteAppsFlow().first().toMutableList()
        val appIndex = apps.indexOfFirst { it.packageName == app.packageName }
        val withAppIndex = apps.indexOfFirst { it.packageName == withApp.packageName }
        if (appIndex == -1 || withAppIndex == -1) return

        apps[appIndex] = favoriteToRoomMapper.toEntity(withApp)
        apps[withAppIndex] = favoriteToRoomMapper.toEntity(app)

        favoriteAppsDao.clearFavoriteApps()
        favoriteAppsDao.addFavorites(apps)
    }

    suspend fun removeFromFavorites(packageName: String) {
        val appRoom = appsDao.getAppBy(packageName) ?: throw IllegalStateException("$packageName app was not found in Database")
        val app = appToRoomMapper.fromEntity(appRoom)
        val favoriteAppRoom = favoriteToRoomMapper.toEntity(app)
        favoriteAppsDao.removeFavorite(favoriteAppRoom)
    }

    suspend fun clearFavorites() = favoriteAppsDao.clearFavoriteApps()
    suspend fun isFavorite(packageName: String) = favoriteAppsDao.getFavoriteAppBy(packageName) != null
}
