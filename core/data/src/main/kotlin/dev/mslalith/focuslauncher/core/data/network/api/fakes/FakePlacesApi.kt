package dev.mslalith.focuslauncher.core.data.network.api.fakes

import dev.mslalith.focuslauncher.core.data.network.api.PlacesApi
import dev.mslalith.focuslauncher.core.data.network.entities.PlaceResponse
import dev.mslalith.focuslauncher.core.data.utils.dummyPlaceResponseFor
import dev.mslalith.focuslauncher.core.model.location.LatLng

internal class FakePlacesApi : PlacesApi {
    override suspend fun getPlace(latLng: LatLng): PlaceResponse = dummyPlaceResponseFor(latLng = latLng)
}
