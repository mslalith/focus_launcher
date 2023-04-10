package dev.mslalith.focuslauncher.core.data.network.api

import dev.mslalith.focuslauncher.core.data.network.entities.PlaceResponse
import dev.mslalith.focuslauncher.core.model.location.LatLng

internal interface PlacesApi {
    suspend fun getPlace(latLng: LatLng): PlaceResponse?
}
