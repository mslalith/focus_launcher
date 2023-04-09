package dev.mslalith.focuslauncher.core.data.repository.impl

import dev.mslalith.focuslauncher.core.data.database.dao.CitiesDao
import dev.mslalith.focuslauncher.core.data.database.dao.PlacesDao
import dev.mslalith.focuslauncher.core.data.dto.toCity
import dev.mslalith.focuslauncher.core.data.dto.toCityRoom
import dev.mslalith.focuslauncher.core.data.dto.toPlace
import dev.mslalith.focuslauncher.core.data.dto.toPlaceRoom
import dev.mslalith.focuslauncher.core.data.network.api.PlacesApi
import dev.mslalith.focuslauncher.core.data.repository.PlacesRepo
import dev.mslalith.focuslauncher.core.model.City
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.model.place.Place
import javax.inject.Inject

internal class PlacesRepoImpl @Inject internal constructor(
    private val placesApi: PlacesApi,
    private val citiesDao: CitiesDao,
    private val placesDao: PlacesDao
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

    override suspend fun fetchAddressLocal(latLng: LatLng): Place? = placesDao.fetchPlace(
        latitude = latLng.latitude.toString(),
        longitude = latLng.longitude.toString()
    )?.toPlace()

    override suspend fun fetchAddress(latLng: LatLng): Place? {
        val localAddress = fetchAddressLocal(latLng)
        if (localAddress != null) return localAddress

        val placeResponse = placesApi.getAddress(latLng = latLng) ?: return null
        placesDao.insertPlace(place = placeResponse.toPlaceRoom())
        return placeResponse.toPlace()
    }
}
