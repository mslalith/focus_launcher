package dev.mslalith.focuslauncher.core.data.network.api.impl

import dev.mslalith.focuslauncher.core.data.network.api.PlacesApi
import dev.mslalith.focuslauncher.core.data.network.entities.PlaceResponse
import dev.mslalith.focuslauncher.core.model.location.LatLng
import io.ktor.client.HttpClient
import io.ktor.client.call.DoubleReceiveException
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.JsonConvertException
import javax.inject.Inject

internal class PlacesApiImpl @Inject constructor(
    private val httpClient: HttpClient
) : PlacesApi {

    override suspend fun getPlace(latLng: LatLng): PlaceResponse? = try {
        httpClient.get(urlString = "https://nominatim.openstreetmap.org/reverse") {
            parameter(key = "format", value = "json")
            parameter(key = "lat", value = latLng.latitude)
            parameter(key = "lon", value = latLng.longitude)
        }.body()
    } catch (e: JsonConvertException) {
        null
    } catch (e: NoTransformationFoundException) {
        null
    } catch (e: DoubleReceiveException) {
        null
    } catch (e: HttpRequestTimeoutException) {
        null
    }
}
