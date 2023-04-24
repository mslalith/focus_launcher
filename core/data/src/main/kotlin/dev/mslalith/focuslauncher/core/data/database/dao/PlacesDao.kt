package dev.mslalith.focuslauncher.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.mslalith.focuslauncher.core.data.database.entities.PlaceRoom
import dev.mslalith.focuslauncher.core.model.Constants.Database.PLACES_TABLE_NAME

@Dao
internal interface PlacesDao {

    @Query(value = "SELECT * FROM $PLACES_TABLE_NAME WHERE latitude = :latitude AND longitude = :longitude")
    suspend fun fetchPlace(latitude: String, longitude: String): PlaceRoom?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlace(place: PlaceRoom)
}
