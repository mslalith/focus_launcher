package dev.mslalith.focuslauncher.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.data.model.places.City
import dev.mslalith.focuslauncher.data.repository.PlacesRepo
import dev.mslalith.focuslauncher.data.repository.settings.LunarPhaseSettingsRepo
import dev.mslalith.focuslauncher.data.utils.AppCoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PickPlaceViewModel @Inject constructor(
    private val placesRepo: PlacesRepo,
    private val lunarPhaseSettingsRepo: LunarPhaseSettingsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    private val _searchQueryStateFlow = MutableStateFlow("")
    val searchQueryStateFlow: StateFlow<String>
        get() = _searchQueryStateFlow

    val citiesStateFlow = placesRepo.citiesFlow.combine(_searchQueryStateFlow) { cities, query ->
        if (query.isEmpty()) return@combine cities
        cities.filter { it.name.contains(query, ignoreCase = true) }
    }.withinScope(emptyList())

    private val _pickedCity = MutableStateFlow<City?>(null)
    val pickedCity: StateFlow<City?>
        get() = _pickedCity

    fun fetchCities() {
        launch { placesRepo.fetchCities() }
    }

    fun updateSearch(query: String) {
        _searchQueryStateFlow.value = query
    }

    fun pickCity(
        city: City,
        onPicked: () -> Unit
    ) {
        _pickedCity.value = city
        launch {
            lunarPhaseSettingsRepo.updatePlace(city)
            withContext(appCoroutineDispatcher.main) { onPicked() }
        }
    }

    private fun launch(
        run: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(appCoroutineDispatcher.io) { run() }

    private fun <T> Flow<T>.withinScope(
        initialValue: T
    ) = stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = initialValue
    )
}
