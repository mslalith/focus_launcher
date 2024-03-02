package dev.mslalith.focuslauncher.core.model

import dev.mslalith.focuslauncher.core.model.location.LatLng
import kotlinx.serialization.Serializable

@Serializable
data class CurrentPlace(
    val latLng: LatLng,
    val address: String
)
