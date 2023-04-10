package dev.mslalith.focuslauncher.core.model.location

import kotlinx.serialization.Serializable

@Serializable
data class LatLng(
    val latitude: Double,
    val longitude: Double
)
