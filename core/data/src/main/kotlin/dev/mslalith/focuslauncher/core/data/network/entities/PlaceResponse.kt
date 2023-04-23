package dev.mslalith.focuslauncher.core.data.network.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PlaceResponse(
    @SerialName("place_id")
    val id: Long,
    @SerialName("licence")
    val license: String,
    @SerialName("osm_type")
    val osmType: String,
    @SerialName("osm_id")
    val osmId: Long,
    @SerialName("lat")
    val latitude: String,
    @SerialName("lon")
    val longitude: String,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("address")
    val address: AddressResponse,
    @SerialName("boundingbox")
    val boundingBox: List<String>
)

@Serializable
internal data class AddressResponse(
    @SerialName("house_number")
    val houseNumber: String? = null,
    @SerialName("road")
    val road: String? = null,
    @SerialName("village")
    val village: String? = null,
    @SerialName("suburb")
    val suburb: String? = null,
    @SerialName("postcode")
    val postCode: String? = null,
    @SerialName("county")
    val county: String? = null,
    @SerialName("state_district")
    val stateDistrict: String? = null,
    @SerialName("state")
    val state: String? = null,
    @SerialName("ISO3166-2-lvl4")
    val iso3166: String? = null,
    @SerialName("country")
    val country: String? = null,
    @SerialName("country_code")
    val countryCode: String? = null
)
