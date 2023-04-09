package dev.mslalith.focuslauncher.core.data.repository.impl

import dev.mslalith.focuslauncher.core.data.database.dao.CitiesDao
import dev.mslalith.focuslauncher.core.data.dto.toCity
import dev.mslalith.focuslauncher.core.data.dto.toCityRoom
import dev.mslalith.focuslauncher.core.data.dto.toPlace
import dev.mslalith.focuslauncher.core.data.network.api.PlacesApi
import dev.mslalith.focuslauncher.core.data.repository.PlacesRepo
import dev.mslalith.focuslauncher.core.model.City
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.model.place.Place
import javax.inject.Inject

internal class PlacesRepoImpl @Inject internal constructor(
    private val placesApi: PlacesApi,
    private val citiesDao: CitiesDao
) : PlacesRepo {

    override suspend fun getAllCities(): List<City> {
        return citiesDao.getAllCities().map { it.toCity() }
    }

    override suspend fun getCitiesByQuery(query: String): List<City> {
        return citiesDao.getCitiesByQuery(query).map { it.toCity() }
    }

    override suspend fun fetchCities() {
        val cities = placesApi.getCities()
        citiesDao.insertCities(cities.map { it.toCityRoom() })
    }

    override suspend fun fetchAddress(latLng: LatLng): Place {
        return placesApi.getAddress(latLng = latLng).toPlace()
    }
}
