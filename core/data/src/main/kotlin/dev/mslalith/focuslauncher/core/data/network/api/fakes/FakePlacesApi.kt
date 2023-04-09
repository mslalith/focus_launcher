package dev.mslalith.focuslauncher.core.data.network.api.fakes

import dev.mslalith.focuslauncher.core.data.network.api.PlacesApi
import dev.mslalith.focuslauncher.core.data.network.entities.AddressResponse
import dev.mslalith.focuslauncher.core.data.network.entities.PlaceResponse
import dev.mslalith.focuslauncher.core.data.utils.dummyCityResponseFor
import dev.mslalith.focuslauncher.core.model.location.LatLng

internal class FakePlacesApi : PlacesApi {
    override suspend fun getCities() = List(size = 6) { dummyCityResponseFor(index = it) }
    override suspend fun getAddress(latLng: LatLng): PlaceResponse =
        PlaceResponse(
            id = 0,
            license = "",
            osmType = "",
            osmId = 0,
            latitude = "0.0",
            longitude = "0.0",
            displayName = "",
            address = AddressResponse(),
            boundingBox = emptyList()
        )
}
