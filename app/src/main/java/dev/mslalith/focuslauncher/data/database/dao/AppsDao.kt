package dev.mslalith.focuslauncher.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.mslalith.focuslauncher.data.database.entities.App
import dev.mslalith.focuslauncher.utils.Constants.Database.APPS_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface AppsDao {

    @Query("SELECT * FROM $APPS_TABLE_NAME")
    fun getAllAppsFlow(): Flow<List<App>>

    @Query("SELECT * FROM $APPS_TABLE_NAME")
    suspend fun getAllApps(): List<App>

    @Query("SELECT * FROM $APPS_TABLE_NAME WHERE packageName = :packageName LIMIT 1")
    suspend fun getAppBy(packageName: String): App?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addApps(apps: List<App>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addApp(app: App)

    @Delete
    suspend fun removeApp(app: App)
}
