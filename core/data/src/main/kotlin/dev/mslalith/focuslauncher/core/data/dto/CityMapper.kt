package dev.mslalith.focuslauncher.core.data.dto

import dev.mslalith.focuslauncher.core.data.database.entities.CityRoom
import dev.mslalith.focuslauncher.core.data.network.entities.CityResponse
import dev.mslalith.focuslauncher.core.model.City

internal fun CityResponse.toCityRoom() = CityRoom(
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

internal fun CityRoom.toCity() = City(
    id = id,
    name = name,
    latitude = latitude.toDouble(),
    longitude = longitude.toDouble()
)
