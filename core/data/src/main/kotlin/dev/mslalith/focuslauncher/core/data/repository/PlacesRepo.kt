package dev.mslalith.focuslauncher.core.data.repository

import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.model.place.Place

interface PlacesRepo {
    suspend fun fetchAddressLocal(latLng: LatLng): Place?
    suspend fun fetchAddress(latLng: LatLng): Place?
}
