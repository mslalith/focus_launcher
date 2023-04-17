package dev.mslalith.focuslauncher.core.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.mslalith.focuslauncher.core.model.Constants.Database.PLACES_TABLE_NAME

@Entity(tableName = PLACES_TABLE_NAME)
internal data class PlaceRoom(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val license: String,
    val osmType: String,
    val osmId: Long,
    val latitude: String,
    val longitude: String,
    val displayName: String,
    @Embedded val address: AddressRoom,
    val boundingBox: List<String>
)

internal data class AddressRoom(
    val houseNumber: String? = null,
    val road: String? = null,
    val village: String? = null,
    val suburb: String? = null,
    val postCode: String? = null,
    val county: String? = null,
    val stateDistrict: String? = null,
    val state: String? = null,
    val iso3166: String? = null,
    val country: String? = null,
    val countryCode: String? = null,
)
