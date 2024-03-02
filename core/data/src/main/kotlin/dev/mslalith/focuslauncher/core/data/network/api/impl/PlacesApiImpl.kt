package dev.mslalith.focuslauncher.core.data.network.api.impl

import dev.mslalith.focuslauncher.core.data.network.api.PlacesApi
import dev.mslalith.focuslauncher.core.data.network.entities.PlaceResponse
import dev.mslalith.focuslauncher.core.model.location.LatLng
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

internal class PlacesApiImpl @Inject constructor(
    private val httpClient: HttpClient
) : PlacesApi {

    override suspend fun getPlace(latLng: LatLng): Result<PlaceResponse> = runCatching {
        httpClient.get(urlString = "https://nominatim.openstreetmap.org/reverse") {
            parameter(key = "format", value = "json")
            parameter(key = "lat", value = latLng.latitude)
            parameter(key = "lon", value = latLng.longitude)
        }.body<PlaceResponse>()
    }.recover { PlaceResponse.default() }
}
