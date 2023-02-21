package dev.mslalith.focuslauncher.core.data.repository

import dev.mslalith.focuslauncher.core.model.City

interface PlacesRepo {
    suspend fun getAllCities(): List<City>
    suspend fun getCitiesByQuery(query: String): List<City>
    suspend fun fetchCities()
}
