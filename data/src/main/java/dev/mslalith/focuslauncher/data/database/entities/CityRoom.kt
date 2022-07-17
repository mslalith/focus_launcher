package dev.mslalith.focuslauncher.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.mslalith.focuslauncher.data.utils.Constants

@Entity(tableName = Constants.Database.CITIES_TABLE_NAME)
data class CityRoom(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val stateId: Int,
    val stateCode: String,
    val stateName: String,
    val countryId: Int,
    val countryCode: String,
    val countryName: String,
    val latitude: String,
    val longitude: String,
    val wikiDataId: String?,
)
