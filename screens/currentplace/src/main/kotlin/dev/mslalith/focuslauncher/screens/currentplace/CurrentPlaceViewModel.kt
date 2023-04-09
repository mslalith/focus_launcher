package dev.mslalith.focuslauncher.screens.currentplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.data.repository.PlacesRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.LunarPhaseSettingsRepo
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.LunarPhase.DEFAULT_CURRENT_PLACE_2
import dev.mslalith.focuslauncher.core.model.CurrentPlace
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.ui.extensions.withinScope
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

@OptIn(FlowPreview::class)
@HiltViewModel
internal class CurrentPlaceViewModel @Inject constructor(
    private val placesRepo: PlacesRepo,
    private val lunarPhaseSettingsRepo: LunarPhaseSettingsRepo,
) : ViewModel() {

    private val _latLngStateFlow = MutableStateFlow(DEFAULT_CURRENT_PLACE_2.latLng)
    private val _addressStateFlow = MutableStateFlow(value = "Not Available")

    private val defaultCurrentPlace = CurrentPlace(
        latLng = _latLngStateFlow.value,
        address = _addressStateFlow.value
    )

    val currentPlaceStateFlow = flowOf(value = defaultCurrentPlace)
        .onStart { fetchCurrentPlaceAndUpdateFlows() }
        .combine(_latLngStateFlow) { state, latLng ->
            state.copy(latLng = latLng)
        }.combine(_addressStateFlow) { state, address ->
            state.copy(address = address)
        }.withinScope(initialValue = defaultCurrentPlace)

    init {
        _latLngStateFlow
            .debounce(timeoutMillis = 1200)
            .drop(count = 1)
            .mapNotNull { placesRepo.fetchAddress(it) }
            .onEach { _addressStateFlow.value = it.displayName }
            .launchIn(viewModelScope)
    }

    suspend fun fetchCurrentPlaceFromDb(): CurrentPlace = lunarPhaseSettingsRepo.currentPlace.firstOrNull() ?: defaultCurrentPlace

    private suspend fun fetchCurrentPlaceAndUpdateFlows() {
        val currentPlace = fetchCurrentPlaceFromDb()
        _latLngStateFlow.value = currentPlace.latLng
        _addressStateFlow.value = currentPlace.address
    }

    fun updateCurrentLocation(latLng: LatLng) {
        _latLngStateFlow.value = latLng
    }

    suspend fun savePlace() {
        lunarPhaseSettingsRepo.updateCurrentPlace(currentPlaceStateFlow.value)
    }
}
