package dev.mslalith.focuslauncher.core.model.location

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class LatLng(
    val latitude: Double,
    val longitude: Double
)
