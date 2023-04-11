package dev.mslalith.focuslauncher.screens.currentplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.LoadingState
import dev.mslalith.focuslauncher.core.common.getOrNull
import dev.mslalith.focuslauncher.core.data.repository.PlacesRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.LunarPhaseSettingsRepo
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.LunarPhase.DEFAULT_CURRENT_PLACE
import dev.mslalith.focuslauncher.core.model.CurrentPlace
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.ui.extensions.withinScope
import dev.mslalith.focuslauncher.screens.currentplace.model.CurrentPlaceState
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
internal class CurrentPlaceViewModel @Inject constructor(
    private val placesRepo: PlacesRepo,
    private val lunarPhaseSettingsRepo: LunarPhaseSettingsRepo,
) : ViewModel() {

    private val defaultAddress = DEFAULT_CURRENT_PLACE.address

    private val _latLngStateFlow: MutableStateFlow<LatLng> = MutableStateFlow(value = DEFAULT_CURRENT_PLACE.latLng)
    private val _addressStateFlow: MutableStateFlow<LoadingState<String>> = MutableStateFlow(value = LoadingState.Loading)

    private val defaultCurrentPlace = CurrentPlace(
        latLng = _latLngStateFlow.value,
        address = defaultAddress
    )

    private val defaultCurrentPlaceState = CurrentPlaceState(
        latLng = DEFAULT_CURRENT_PLACE.latLng,
        addressState = LoadingState.Loading,
        canSave = false
    )

    val currentPlaceState = flowOf(value = defaultCurrentPlaceState)
        .combine(_latLngStateFlow) { state, latLng ->
            state.copy(latLng = latLng)
        }.combine(_addressStateFlow) { state, addressState ->
            state.copy(
                addressState = addressState,
                canSave = addressState is LoadingState.Loaded
            )
        }.withinScope(initialValue = defaultCurrentPlaceState)

    init {
        _latLngStateFlow
            .mapLatest(::fetchAddressAndUpdateFlows)
            .launchIn(viewModelScope)
    }

    suspend fun fetchCurrentPlaceFromDb(): CurrentPlace = lunarPhaseSettingsRepo.currentPlaceFlow.firstOrNull() ?: defaultCurrentPlace

    private suspend fun fetchAddressAndUpdateFlows(latLng: LatLng) {
        _addressStateFlow.value = LoadingState.Loading
        val place = placesRepo.fetchPlace(latLng = latLng)
        val address = place?.displayName ?: defaultAddress
        _addressStateFlow.value = LoadingState.Loaded(value = address)
    }

    fun updateCurrentLatLng(latLng: LatLng) {
        _latLngStateFlow.value = latLng
    }

    suspend fun savePlace() {
        val address = _addressStateFlow.value.getOrNull() ?: return
        val currentPlace = CurrentPlace(
            latLng = _latLngStateFlow.value,
            address = address
        )
        lunarPhaseSettingsRepo.updateCurrentPlace(currentPlace = currentPlace)
    }
}
