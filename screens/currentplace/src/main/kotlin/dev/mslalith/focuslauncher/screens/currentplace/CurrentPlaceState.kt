package dev.mslalith.focuslauncher.screens.currentplace

import com.slack.circuit.runtime.CircuitUiState
import dev.mslalith.focuslauncher.core.common.model.LoadingState
import dev.mslalith.focuslauncher.core.model.location.LatLng

data class CurrentPlaceState(
    val latLng: LatLng,
    val initialLatLng: LatLng,
    val addressState: LoadingState<String>,
    val isOnline: Boolean,
    val canSave: Boolean,
    val eventSink: (CurrentPlaceUiEvent) -> Unit
) : CircuitUiState
