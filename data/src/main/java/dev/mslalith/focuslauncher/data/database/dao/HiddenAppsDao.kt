package dev.mslalith.focuslauncher.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.mslalith.focuslauncher.data.database.entities.HiddenAppRoom
import dev.mslalith.focuslauncher.data.utils.Constants.Database.HIDDEN_APPS_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface HiddenAppsDao {

    @Query("SELECT * FROM $HIDDEN_APPS_TABLE_NAME")
    fun getHiddenAppsFlow(): Flow<List<HiddenAppRoom>>

    @Query("SELECT * FROM $HIDDEN_APPS_TABLE_NAME WHERE packageName = :packageName LIMIT 1")
    suspend fun getHiddenAppBy(packageName: String): HiddenAppRoom?

    @Query("DELETE FROM $HIDDEN_APPS_TABLE_NAME")
    suspend fun clearHiddenApps()

    @Query("SELECT COUNT(*) $HIDDEN_APPS_TABLE_NAME")
    suspend fun getHiddenAppsSize(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun hideApp(hiddenAppRoom: HiddenAppRoom)

    @Delete
    suspend fun unHideApp(hiddenAppRoom: HiddenAppRoom)
}
