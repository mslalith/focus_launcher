package dev.mslalith.focuslauncher.core.data.network.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CityResponse(
    @SerialName(value = "id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("state_id")
    val stateId: Int,
    @SerialName("state_code")
    val stateCode: String,
    @SerialName("state_name")
    val stateName: String,
    @SerialName("country_id")
    val countryId: Int,
    @SerialName("country_code")
    val countryCode: String,
    @SerialName("country_name")
    val countryName: String,
    @SerialName("latitude")
    val latitude: String,
    @SerialName("longitude")
    val longitude: String,
    @SerialName("wikiDataId")
    val wikiDataId: String?
)
