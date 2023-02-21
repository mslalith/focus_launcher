package dev.mslalith.focuslauncher.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.PlacesRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.LunarPhaseSettingsRepo
import dev.mslalith.focuslauncher.core.model.City
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class PickPlaceViewModel @Inject constructor(
    private val placesRepo: PlacesRepo,
    private val lunarPhaseSettingsRepo: LunarPhaseSettingsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    private val _searchQueryStateFlow = MutableStateFlow("")
    val searchQueryStateFlow: StateFlow<String>
        get() = _searchQueryStateFlow

    private val _citiesStateFlow = MutableStateFlow<List<City>>(emptyList())
    val citiesStateFlow: StateFlow<List<City>>
        get() = _citiesStateFlow

    fun fetchCities() {
        launch { placesRepo.fetchCities() }
    }

    fun updateSearch(query: String) {
        _searchQueryStateFlow.value = query
        launch {
            _citiesStateFlow.value = if (query.isEmpty()) placesRepo.getAllCities() else placesRepo.getCitiesByQuery(query)
        }
    }

    fun pickCity(
        city: City,
        onPicked: () -> Unit
    ) {
        launch {
            lunarPhaseSettingsRepo.updatePlace(city)
            withContext(appCoroutineDispatcher.main) { onPicked() }
        }
    }

    private fun launch(
        run: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(appCoroutineDispatcher.io) { run() }
}
