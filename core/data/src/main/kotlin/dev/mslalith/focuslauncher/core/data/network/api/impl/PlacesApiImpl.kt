package dev.mslalith.focuslauncher.core.data.network.api.impl

import dev.mslalith.focuslauncher.core.data.network.api.PlacesApi
import dev.mslalith.focuslauncher.core.data.network.entities.PlaceResponse
import dev.mslalith.focuslauncher.core.model.location.LatLng
import io.ktor.client.HttpClient
import io.ktor.client.call.DoubleReceiveException
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

internal class PlacesApiImpl @Inject constructor(
    private val httpClient: HttpClient
) : PlacesApi {

    override suspend fun getPlace(latLng: LatLng): PlaceResponse? = try {
        httpClient.get("https://nominatim.openstreetmap.org/reverse") {
            parameter("format", "json")
            parameter("lat", latLng.latitude)
            parameter("lon", latLng.longitude)
        }.body()
    } catch (e: NoTransformationFoundException) {
        null
    } catch (e: DoubleReceiveException) {
        null
    }
}
