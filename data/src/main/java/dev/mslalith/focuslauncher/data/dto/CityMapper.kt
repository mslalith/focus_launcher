package dev.mslalith.focuslauncher.data.dto

import dev.mslalith.focuslauncher.core.model.City
import dev.mslalith.focuslauncher.data.database.entities.CityRoom
import dev.mslalith.focuslauncher.data.network.entities.CityResponse

fun CityResponse.toCityRoom() = CityRoom(
    id = id,
    name = name,
    stateId = stateId,
    stateCode = stateCode,
    stateName = stateName,
    countryId = countryId,
    countryCode = countryCode,
    countryName = countryName,
    latitude = latitude,
    longitude = longitude,
    wikiDataId = wikiDataId
)

fun CityRoom.toCity() = City(
    id = id,
    name = name,
    latitude = latitude.toDouble(),
    longitude = longitude.toDouble()
)
