package dev.mslalith.focuslauncher.screens.currentplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.LoadingState
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.common.model.getOrNull
import dev.mslalith.focuslauncher.core.data.repository.PlacesRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.LunarPhaseSettingsRepo
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.LunarPhase.DEFAULT_CURRENT_PLACE
import dev.mslalith.focuslauncher.core.model.CurrentPlace
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.ui.extensions.launchInIO
import dev.mslalith.focuslauncher.core.ui.extensions.withinScope
import dev.mslalith.focuslauncher.screens.currentplace.model.CurrentPlaceState
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
internal class CurrentPlaceViewModel @Inject constructor(
    private val placesRepo: PlacesRepo,
    private val lunarPhaseSettingsRepo: LunarPhaseSettingsRepo,
    appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    private val _initialLatLngStateFlow: MutableStateFlow<LatLng> = MutableStateFlow(value = DEFAULT_CURRENT_PLACE.latLng)
    private val _latLngStateFlow: MutableStateFlow<LatLng> = MutableStateFlow(value = DEFAULT_CURRENT_PLACE.latLng)
    private val _addressStateFlow: MutableStateFlow<LoadingState<String?>> = MutableStateFlow(value = LoadingState.Loading)

    private val defaultCurrentPlace = CurrentPlace(
        latLng = _latLngStateFlow.value,
        address = DEFAULT_CURRENT_PLACE.address
    )

    private val defaultCurrentPlaceState = CurrentPlaceState(
        latLng = _latLngStateFlow.value,
        initialLatLng = _initialLatLngStateFlow.value,
        addressState = LoadingState.Loading,
        canSave = false
    )

    val currentPlaceState = flowOf(value = defaultCurrentPlaceState)
        .combine(flow = _initialLatLngStateFlow) { state, initialLatLng ->
            state.copy(initialLatLng = initialLatLng)
        }.combine(flow = _latLngStateFlow) { state, latLng ->
            state.copy(latLng = latLng)
        }.combine(flow = _addressStateFlow) { state, addressState ->
            state.copy(
                addressState = addressState,
                canSave = addressState is LoadingState.Loaded
            )
        }.withinScope(initialValue = defaultCurrentPlaceState)

    init {
        _latLngStateFlow
            .mapLatest(::fetchAddressAndUpdateFlows)
            .flowOn(context = appCoroutineDispatcher.io)
            .launchIn(scope = viewModelScope)

        appCoroutineDispatcher.launchInIO {
            val currentPlace = lunarPhaseSettingsRepo.currentPlaceFlow.firstOrNull() ?: defaultCurrentPlace
            _initialLatLngStateFlow.value = currentPlace.latLng
        }
    }

    private suspend fun fetchAddressAndUpdateFlows(latLng: LatLng) {
        _addressStateFlow.value = LoadingState.Loading
        val place = placesRepo.fetchPlace(latLng = latLng)
        val address = place?.displayName ?: defaultCurrentPlace.address
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
