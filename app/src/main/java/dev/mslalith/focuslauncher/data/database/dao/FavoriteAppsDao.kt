package dev.mslalith.focuslauncher.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.mslalith.focuslauncher.data.database.entities.FavoriteApp
import dev.mslalith.focuslauncher.utils.Constants.Database.FAVORITE_APPS_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteAppsDao {

    @Query("SELECT * FROM $FAVORITE_APPS_TABLE_NAME")
    fun getFavoriteAppsFlow(): Flow<List<FavoriteApp>>

    @Query("SELECT * FROM $FAVORITE_APPS_TABLE_NAME WHERE packageName = :packageName LIMIT 1")
    suspend fun getFavoriteAppBy(packageName: String): FavoriteApp?

    @Query("DELETE FROM $FAVORITE_APPS_TABLE_NAME")
    suspend fun clearFavoriteApps()

    @Query("SELECT COUNT(*) $FAVORITE_APPS_TABLE_NAME")
    suspend fun getFavoriteAppsSize(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favoriteApp: FavoriteApp)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorites(favoriteApps: List<FavoriteApp>)

    @Delete
    suspend fun removeFavorite(favoriteApp: FavoriteApp)
}
