package dev.mslalith.focuslauncher.data.repository

import dev.mslalith.focuslauncher.data.database.dao.CitiesDao
import dev.mslalith.focuslauncher.data.dto.places.toCity
import dev.mslalith.focuslauncher.data.dto.places.toCityRoom
import dev.mslalith.focuslauncher.data.network.api.PlacesApi
import javax.inject.Inject

class PlacesRepo @Inject constructor(
    private val placesApi: PlacesApi,
    private val citiesDao: CitiesDao
) {

    suspend fun getAllCities() = citiesDao.getAllCities().map { it.toCity() }

    suspend fun getCitiesByQuery(query: String) = citiesDao.getCitiesByQuery(query).map { it.toCity() }

    suspend fun fetchCities() {
        val cities = placesApi.getCities()
        citiesDao.insertCities(cities.map { it.toCityRoom() })
    }
}
