package dev.mslalith.focuslauncher.screens.currentplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.LoadingState
import dev.mslalith.focuslauncher.core.data.repository.PlacesRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.LunarPhaseSettingsRepo
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.LunarPhase.DEFAULT_CURRENT_PLACE
import dev.mslalith.focuslauncher.core.model.CurrentPlace
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.ui.extensions.withinScope
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
internal class CurrentPlaceViewModel @Inject constructor(
    private val placesRepo: PlacesRepo,
    private val lunarPhaseSettingsRepo: LunarPhaseSettingsRepo,
) : ViewModel() {

    private val defaultAddress = "Not Available"

    private val _latLngStateFlow = MutableStateFlow(value = DEFAULT_CURRENT_PLACE.latLng)
    private val _addressStateFlow: MutableStateFlow<LoadingState<String>> = MutableStateFlow(value = LoadingState.Loaded(defaultAddress))
    val addressStateFlow = _addressStateFlow.asStateFlow()

    private val defaultCurrentPlace = CurrentPlace(
        latLng = _latLngStateFlow.value,
        address = defaultAddress
    )

    val currentPlaceStateFlow = flowOf(value = defaultCurrentPlace)
        .onStart { fetchCurrentPlaceAndUpdateFlows() }
        .combine(_latLngStateFlow) { state, latLng ->
            state.copy(latLng = latLng)
        }.combine(_addressStateFlow) { state, addressState ->
            when (addressState) {
                is LoadingState.Loaded -> state.copy(address = addressState.value)
                LoadingState.Loading -> state
            }
        }.withinScope(initialValue = defaultCurrentPlace)

    init {
        _latLngStateFlow
            .debounce(timeoutMillis = 1200)
            .drop(count = 1)
            .mapLatest(::fetchAddressAndUpdateFlows)
            .launchIn(viewModelScope)
    }

    suspend fun fetchCurrentPlaceFromDb(): CurrentPlace = lunarPhaseSettingsRepo.currentPlaceFlow.firstOrNull() ?: defaultCurrentPlace

    private suspend fun fetchCurrentPlaceAndUpdateFlows() {
        val currentPlace = fetchCurrentPlaceFromDb()
        _latLngStateFlow.value = currentPlace.latLng
        _addressStateFlow.value = LoadingState.Loaded(value = currentPlace.address)
    }

    private suspend fun fetchAddressAndUpdateFlows(latLng: LatLng) {
        _addressStateFlow.value = LoadingState.Loading
        val place = placesRepo.fetchAddress(latLng = latLng)
        val address = place?.displayName ?: defaultAddress
        _addressStateFlow.value = LoadingState.Loaded(value = address)
    }

    fun updateCurrentLocation(latLng: LatLng) {
        _latLngStateFlow.value = latLng
    }

    suspend fun savePlace() {
        lunarPhaseSettingsRepo.updateCurrentPlace(currentPlaceStateFlow.value)
    }
}
