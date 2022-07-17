package dev.mslalith.focuslauncher.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.mslalith.focuslauncher.data.database.entities.CityRoom
import dev.mslalith.focuslauncher.data.utils.Constants

@Dao
interface CitiesDao {

    @Query("SELECT * FROM ${Constants.Database.CITIES_TABLE_NAME} ORDER BY id")
    suspend fun getAllCities(): List<CityRoom>

    @Query("SELECT * FROM ${Constants.Database.CITIES_TABLE_NAME} WHERE name LIKE '%' || :query || '%' ORDER BY id")
    suspend fun getCitiesByQuery(query: String): List<CityRoom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCities(cities: List<CityRoom>)
}
