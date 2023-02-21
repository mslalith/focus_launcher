package dev.mslalith.focuslauncher.core.data.repository

import dev.mslalith.focuslauncher.core.model.App
import kotlinx.coroutines.flow.Flow

interface FavoritesRepo {
    val onlyFavoritesFlow: Flow<List<App>>

    suspend fun addToFavorites(app: App)
    suspend fun addToFavorites(apps: List<App>)
    suspend fun reorderFavorite(app: App, withApp: App)
    suspend fun removeFromFavorites(packageName: String)
    suspend fun clearFavorites()
    suspend fun isFavorite(packageName: String): Boolean
}
