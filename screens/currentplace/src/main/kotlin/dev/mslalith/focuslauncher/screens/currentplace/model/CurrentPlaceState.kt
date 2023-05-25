package dev.mslalith.focuslauncher.screens.currentplace.model

import androidx.compose.runtime.Immutable
import dev.mslalith.focuslauncher.core.common.model.LoadingState
import dev.mslalith.focuslauncher.core.model.location.LatLng

@Immutable
internal data class CurrentPlaceState(
    val latLng: LatLng,
    val initialLatLng: LatLng,
    val addressState: LoadingState<String?>,
    val canSave: Boolean
)
