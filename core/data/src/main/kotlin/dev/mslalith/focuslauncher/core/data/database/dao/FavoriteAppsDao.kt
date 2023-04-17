package dev.mslalith.focuslauncher.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.mslalith.focuslauncher.core.data.database.entities.FavoriteAppRoom
import dev.mslalith.focuslauncher.core.model.Constants.Database.FAVORITE_APPS_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
internal interface FavoriteAppsDao {

    @Query("SELECT * FROM $FAVORITE_APPS_TABLE_NAME")
    fun getFavoriteAppsFlow(): Flow<List<FavoriteAppRoom>>

    @Query("SELECT * FROM $FAVORITE_APPS_TABLE_NAME WHERE packageName = :packageName LIMIT 1")
    suspend fun getFavoriteAppBy(packageName: String): FavoriteAppRoom?

    @Query("DELETE FROM $FAVORITE_APPS_TABLE_NAME")
    suspend fun clearFavoriteApps()

    @Query("SELECT COUNT(*) $FAVORITE_APPS_TABLE_NAME")
    suspend fun getFavoriteAppsSize(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favoriteApp: FavoriteAppRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorites(favoriteApps: List<FavoriteAppRoom>)

    @Delete
    suspend fun removeFavorite(favoriteApp: FavoriteAppRoom)
}
