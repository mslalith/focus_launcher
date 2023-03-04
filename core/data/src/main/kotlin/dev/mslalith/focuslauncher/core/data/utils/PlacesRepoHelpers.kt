package dev.mslalith.focuslauncher.core.data.utils

import dev.mslalith.focuslauncher.core.data.network.entities.CityResponse
import dev.mslalith.focuslauncher.core.model.City

internal fun dummyCityResponseFor(index: Int) = CityResponse(
    id = index,
    name = "Name #$index",
    stateId = index,
    stateName = "State Name #$index",
    stateCode = "State Code #$index",
    countryId = index,
    countryCode = "Country Code #$index",
    countryName = "Country Name #$index",
    latitude = "$index.$index",
    longitude = "$index.$index",
    wikiDataId = "Wiki Data #$index"
)

internal fun dummyCityFor(index: Int) = City(
    id = index,
    name = "Name #$index",
    latitude = "$index.$index".toDouble(),
    longitude = "$index.$index".toDouble(),
)
