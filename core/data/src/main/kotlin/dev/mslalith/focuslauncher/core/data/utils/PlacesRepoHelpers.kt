package dev.mslalith.focuslauncher.core.data.utils

import dev.mslalith.focuslauncher.core.data.network.entities.AddressResponse
import dev.mslalith.focuslauncher.core.data.network.entities.PlaceResponse
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.model.place.Address
import dev.mslalith.focuslauncher.core.model.place.Place

internal fun dummyPlaceResponseFor(latLng: LatLng): PlaceResponse {
    val number = latLng.latitude
    return PlaceResponse(
        id = number.toLong(),
        latitude = latLng.latitude.toString(),
        longitude = latLng.longitude.toString(),
        license = "License $number",
        osmType = "OSM Type $number",
        osmId = number.toLong(),
        displayName = "Display Name $number",
        boundingBox = emptyList(),
        address = AddressResponse(
            state= "State $number",
            country = "Country $number"
        )
    )
}

fun dummyPlaceFor(latLng: LatLng): Place {
    val number = latLng.latitude
    return Place(
        id = number.toLong(),
        license = "License $number",
        displayName = "Display Name $number",
        latLng = LatLng(
            latitude = latLng.latitude,
            longitude = latLng.longitude
        ),
        address = Address(
            state= "State $number",
            country = "Country $number"
        )
    )
}
