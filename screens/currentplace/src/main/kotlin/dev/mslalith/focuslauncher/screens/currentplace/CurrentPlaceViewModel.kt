package dev.mslalith.focuslauncher.screens.currentplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.PlacesRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.LunarPhaseSettingsRepo
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.LunarPhase.DEFAULT_CURRENT_PLACE_2
import dev.mslalith.focuslauncher.core.model.CurrentPlace
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.ui.extensions.withinScope
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@HiltViewModel
internal class CurrentPlaceViewModel @Inject constructor(
    private val placesRepo: PlacesRepo,
    private val lunarPhaseSettingsRepo: LunarPhaseSettingsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    private val _latLngStateFlow = MutableStateFlow(DEFAULT_CURRENT_PLACE_2.latLng)
    private val _addressStateFlow = MutableStateFlow(value = "Not Available")

    private val defaultCurrentPlace = CurrentPlace(
        latLng = _latLngStateFlow.value,
        address = _addressStateFlow.value
    )

    val currentPlaceStateFlow = flowOf(value = defaultCurrentPlace)
        .combine(_latLngStateFlow) { state, latLng ->
            state.copy(latLng = latLng)
        }.combine(_addressStateFlow) { state, address ->
            state.copy(address = address)
        }.withinScope(initialValue = defaultCurrentPlace)

    init {
        _latLngStateFlow.debounce(timeoutMillis = 2_000)
            .map { placesRepo.fetchAddress(it) }
            .onEach { _addressStateFlow.value = it.displayName }
            .launchIn(viewModelScope)
    }

    fun updateCurrentLocation(latLng: LatLng) {
        _latLngStateFlow.value = latLng
    }
}
