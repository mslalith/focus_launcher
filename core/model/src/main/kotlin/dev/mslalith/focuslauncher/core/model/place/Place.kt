package dev.mslalith.focuslauncher.core.model.place

import dev.mslalith.focuslauncher.core.model.location.LatLng

data class Place(
    val id: Long,
    val license: String,
    val latLng: LatLng,
    val displayName: String,
    val address: Address
) {
    companion object {
        fun default() = Place(
            id = -1,
            license = "",
            latLng = LatLng(
                latitude = 0.0,
                longitude = 0.0
            ),
            displayName = "",
            address = Address(
                state = null,
                country = null
            )
        )
    }
}
