package dev.mslalith.focuslauncher.core.data.repository

import dev.mslalith.focuslauncher.core.model.City
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.model.place.Place

interface PlacesRepo {
    suspend fun getAllCities(): List<City>
    suspend fun getCitiesByQuery(query: String): List<City>
    suspend fun fetchCities()
    suspend fun fetchAddressLocal(latLng: LatLng): Place?
    suspend fun fetchAddress(latLng: LatLng): Place?
}
