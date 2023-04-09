package dev.mslalith.focuslauncher.core.model.place

import dev.mslalith.focuslauncher.core.model.location.LatLng

data class Place(
    val id: Long,
    val license: String,
    val latLng: LatLng,
    val displayName: String,
    val address: Address
)
