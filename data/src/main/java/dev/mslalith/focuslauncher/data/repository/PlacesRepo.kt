package dev.mslalith.focuslauncher.data.repository

import dev.mslalith.focuslauncher.data.dto.places.toCity
import dev.mslalith.focuslauncher.data.model.places.City
import dev.mslalith.focuslauncher.data.network.api.PlacesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class PlacesRepo @Inject constructor(
    private val placesApi: PlacesApi
) {
    private val _citiesStateFlow = MutableStateFlow<List<City>>(emptyList())
    val citiesStateFlow: StateFlow<List<City>>
        get() = _citiesStateFlow

    suspend fun fetchCities() {
        _citiesStateFlow.value = placesApi.getCities().map { it.toCity() }
    }
}
