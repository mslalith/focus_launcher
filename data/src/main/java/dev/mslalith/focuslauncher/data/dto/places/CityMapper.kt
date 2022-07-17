package dev.mslalith.focuslauncher.data.dto.places

import dev.mslalith.focuslauncher.data.model.places.City
import dev.mslalith.focuslauncher.data.network.entities.CityResponse

fun CityResponse.toCity() = City(
    id = id,
    name = name,
    latitude = latitude.toDouble(),
    longitude = longitude.toDouble()
)
