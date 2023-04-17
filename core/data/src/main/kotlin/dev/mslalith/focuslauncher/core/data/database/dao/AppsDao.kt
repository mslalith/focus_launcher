package dev.mslalith.focuslauncher.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.mslalith.focuslauncher.core.data.database.entities.AppRoom
import dev.mslalith.focuslauncher.core.model.Constants.Database.APPS_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
internal interface AppsDao {

    @Query("SELECT * FROM $APPS_TABLE_NAME")
    fun getAllAppsFlow(): Flow<List<AppRoom>>

    @Query("SELECT * FROM $APPS_TABLE_NAME")
    suspend fun getAllApps(): List<AppRoom>

    @Query("SELECT * FROM $APPS_TABLE_NAME WHERE packageName = :packageName LIMIT 1")
    suspend fun getAppBy(packageName: String): AppRoom?

    @Query("SELECT * FROM $APPS_TABLE_NAME WHERE packageName = :packageName LIMIT 1")
    fun getAppBySync(packageName: String): AppRoom?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addApps(apps: List<AppRoom>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addApp(app: AppRoom)

    @Update
    suspend fun updateApp(app: AppRoom)

    @Delete
    suspend fun removeApp(app: AppRoom)

    @Query("DELETE FROM $APPS_TABLE_NAME")
    suspend fun clearApps()
}
