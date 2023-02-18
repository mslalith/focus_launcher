package dev.mslalith.focuslauncher.core.model

import kotlinx.serialization.Serializable

@Serializable
data class City(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double
)
