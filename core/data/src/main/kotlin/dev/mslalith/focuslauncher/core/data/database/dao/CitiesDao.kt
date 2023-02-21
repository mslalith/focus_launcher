package dev.mslalith.focuslauncher.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.mslalith.focuslauncher.core.data.database.entities.CityRoom
import dev.mslalith.focuslauncher.core.data.utils.Constants.Database.CITIES_TABLE_NAME

@Dao
internal interface CitiesDao {

    @Query("SELECT * FROM $CITIES_TABLE_NAME ORDER BY id")
    suspend fun getAllCities(): List<CityRoom>

    @Query("SELECT * FROM $CITIES_TABLE_NAME WHERE name LIKE '%' || :query || '%' ORDER BY id")
    suspend fun getCitiesByQuery(query: String): List<CityRoom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCities(cities: List<CityRoom>)
}
