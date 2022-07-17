package dev.mslalith.focuslauncher.data.repository

import dev.mslalith.focuslauncher.data.database.dao.CitiesDao
import dev.mslalith.focuslauncher.data.dto.places.toCity
import dev.mslalith.focuslauncher.data.dto.places.toCityRoom
import dev.mslalith.focuslauncher.data.network.api.PlacesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class PlacesRepo @Inject constructor(
    private val placesApi: PlacesApi,
    private val citiesDao: CitiesDao
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    val citiesFlow = citiesDao.getCities().mapLatest { cities ->
        cities.map { it.toCity() }
    }

    suspend fun fetchCities() {
        val cities = placesApi.getCities()
        citiesDao.insertCities(cities.map { it.toCityRoom() })
    }
}
