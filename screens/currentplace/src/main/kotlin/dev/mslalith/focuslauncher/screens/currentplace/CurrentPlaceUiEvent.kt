package dev.mslalith.focuslauncher.screens.currentplace

import com.slack.circuit.runtime.CircuitUiEvent
import dev.mslalith.focuslauncher.core.model.location.LatLng

sealed interface CurrentPlaceUiEvent : CircuitUiEvent {
    data object GoBack : CurrentPlaceUiEvent
    data object SavePlace : CurrentPlaceUiEvent
    data class UpdateCurrentLatLng(val latLng: LatLng) : CurrentPlaceUiEvent
}
