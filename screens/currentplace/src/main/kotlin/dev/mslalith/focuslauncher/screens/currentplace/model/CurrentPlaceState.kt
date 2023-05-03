package dev.mslalith.focuslauncher.screens.currentplace.model

import dev.mslalith.focuslauncher.core.common.LoadingState
import dev.mslalith.focuslauncher.core.model.location.LatLng
import javax.annotation.concurrent.Immutable

@Immutable
internal data class CurrentPlaceState(
    val latLng: LatLng,
    val initialLatLng: LatLng,
    val addressState: LoadingState<String?>,
    val canSave: Boolean
)
