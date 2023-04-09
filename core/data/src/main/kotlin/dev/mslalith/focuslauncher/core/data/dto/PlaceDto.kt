package dev.mslalith.focuslauncher.core.data.dto

import dev.mslalith.focuslauncher.core.data.database.entities.PlaceRoom
import dev.mslalith.focuslauncher.core.data.network.entities.PlaceResponse
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.model.place.Place

internal fun PlaceResponse.toPlace(): Place = Place(
    id = id,
    license = license,
    latLng = LatLng(
        latitude = latitude.toDoubleOrNull() ?: 0.0,
        longitude = longitude.toDoubleOrNull() ?: 0.0
    ),
    displayName = displayName,
    address = address.toAddress()
)

internal fun PlaceRoom.toPlace(): Place = Place(
    id = id,
    license = license,
    latLng = LatLng(
        latitude = latitude.toDoubleOrNull() ?: 0.0,
        longitude = longitude.toDoubleOrNull() ?: 0.0
    ),
    displayName = displayName,
    address = address.toAddress()
)

internal fun PlaceResponse.toPlaceRoom(): PlaceRoom = PlaceRoom(
    id = id,
    license = license,
    osmType = osmType,
    osmId = osmId,
    latitude = latitude,
    longitude = longitude,
    displayName = displayName,
    address = address.toAddressRoom(),
    boundingBox = boundingBox
)
