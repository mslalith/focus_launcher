package dev.mslalith.focuslauncher.data.network.api

import dev.mslalith.focuslauncher.data.network.entities.CityResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import javax.inject.Inject

interface PlacesApi {
    suspend fun getCities(): List<CityResponse>
}

internal class PlacesApiImpl @Inject constructor(
    private val httpClient: HttpClient
) : PlacesApi {

    private val baseUrl = "https://raw.githubusercontent.com/dr5hn/countries-states-cities-database/master"

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getCities(): List<CityResponse> {
        val responseString = httpClient.get("$baseUrl/cities.json").bodyAsChannel()
        return Json.decodeFromStream(
            deserializer = ListSerializer(elementSerializer = CityResponse.serializer()),
            stream = responseString.toInputStream()
        )
    }
}
