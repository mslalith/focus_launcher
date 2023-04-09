package dev.mslalith.focuslauncher.core.data.network.api.impl

import dev.mslalith.focuslauncher.core.data.network.api.PlacesApi
import dev.mslalith.focuslauncher.core.data.network.entities.CityResponse
import dev.mslalith.focuslauncher.core.data.network.entities.PlaceResponse
import dev.mslalith.focuslauncher.core.model.location.LatLng
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import javax.inject.Inject
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

internal class PlacesApiImpl @Inject constructor(
    private val httpClient: HttpClient
) : PlacesApi {

    private val baseUrl = "https://raw.githubusercontent.com/dr5hn/countries-states-cities-database/master"

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getCities(): List<CityResponse> {
        val byteReadChannel = httpClient.get("$baseUrl/cities.json").bodyAsChannel()
        return Json.decodeFromStream(
            deserializer = ListSerializer(elementSerializer = CityResponse.serializer()),
            stream = byteReadChannel.toInputStream()
        )
    }

    override suspend fun getAddress(latLng: LatLng): PlaceResponse? = try {
        httpClient.get("https://nominatim.openstreetmap.org/reverse") {
            parameter("format", "json")
            parameter("lat", latLng.latitude)
            parameter("lon", latLng.longitude)
        }.body()
    } catch (e: Exception) {
        null
    }
}
