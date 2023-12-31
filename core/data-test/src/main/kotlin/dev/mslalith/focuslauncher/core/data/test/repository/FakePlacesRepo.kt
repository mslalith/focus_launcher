package dev.mslalith.focuslauncher.core.data.test.repository

import dev.mslalith.focuslauncher.core.data.repository.PlacesRepo
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.model.place.Place

class FakePlacesRepo : PlacesRepo {

    private var place: Place? = null

    override suspend fun fetchPlaceLocal(latLng: LatLng): Place? = place

    override suspend fun fetchPlace(latLng: LatLng): Place? = place

    fun setPlace(place: Place?) {
        this.place = place
    }
}
