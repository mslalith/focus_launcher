package dev.mslalith.focuslauncher.core.data.repository

import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.model.place.Place

interface PlacesRepo {
    suspend fun fetchPlaceLocal(latLng: LatLng): Place?
    suspend fun fetchPlace(latLng: LatLng): Place?
}
