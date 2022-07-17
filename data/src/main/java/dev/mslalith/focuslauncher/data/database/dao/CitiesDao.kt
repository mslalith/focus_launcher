package dev.mslalith.focuslauncher.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.mslalith.focuslauncher.data.database.entities.CityRoom
import dev.mslalith.focuslauncher.data.utils.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface CitiesDao {

    @Query("SELECT * FROM ${Constants.Database.CITIES_TABLE_NAME}")
    fun getCities(): Flow<List<CityRoom>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCities(cities: List<CityRoom>)
}
