package dev.mslalith.focuslauncher.core.data.network.api.impl

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.core.data.network.entities.PlaceResponse
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.testing.KtorApiTest
import io.ktor.client.call.DoubleReceiveException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.serialization.JsonConvertException
import io.mockk.mockk
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
internal class PlacesApiImplTest : KtorApiTest() {

    private val placesApi = PlacesApiImpl(httpClient = client)

    private val url = "https://nominatim.openstreetmap.org/reverse"

    @Test
    fun `01 - when latLng is 0 & 0, then place must return Soul Buoy`() = runCoroutineTest {
        val latLng = LatLng(latitude = 0.0, longitude = 0.0)

        onRequestTo(url = url) {
            successResponse(content = successJson(latLng = latLng, displayName = "Soul Buoy"))
        }

        placesApi.getPlace(latLng = latLng).assertWith(
            latLng = latLng,
            displayName = "Soul Buoy"
        )
    }

    @Test
    fun `02 - when latLng is 23 & 23, then place must return India`() = runCoroutineTest {
        val latLng = LatLng(latitude = 23.14, longitude = 23.86)

        onRequestTo(url = url) {
            successResponse(content = successJson(latLng = latLng, displayName = "India"))
        }

        placesApi.getPlace(latLng = latLng).assertWith(
            latLng = latLng,
            displayName = "India"
        )
    }

    @Test
    fun `03 - verify JsonConvertException`() = runCoroutineTest {
        val latLng = LatLng(latitude = 23.14, longitude = 23.86)

        onRequestTo(url = url) {
            throw JsonConvertException(message = "Test exception")
        }

        placesApi.getPlace(latLng = latLng).assertException<JsonConvertException>()
    }

    @Test
    fun `04 - verify DoubleReceiveException`() = runCoroutineTest {
        val latLng = LatLng(latitude = 23.14, longitude = 23.86)

        onRequestTo(url = url) {
            throw DoubleReceiveException(call = mockk())
        }

        placesApi.getPlace(latLng = latLng).assertException<DoubleReceiveException>()
    }

    @Test
    fun `05 - verify HttpRequestTimeoutException`() = runCoroutineTest {
        val latLng = LatLng(latitude = 23.14, longitude = 23.86)

        onRequestTo(url = url) {
            throw HttpRequestTimeoutException(url = "", timeoutMillis = null)
        }

        placesApi.getPlace(latLng = latLng).assertException<HttpRequestTimeoutException>()
    }
}

private inline fun <reified T : Exception> Result<PlaceResponse>.assertException() {
    exceptionOrNull().apply {
        assertThat(this).isNotNull()
        assertThat(this).isInstanceOf(T::class.java)
    }
}

private fun Result<PlaceResponse>.assertWith(
    latLng: LatLng,
    displayName: String
) {
    getOrNull().apply {
        assertThat(this).isNotNull()
        assertThat(this?.latitude?.toDouble()).isEqualTo(latLng.latitude)
        assertThat(this?.longitude?.toDouble()).isEqualTo(latLng.longitude)
        assertThat(this?.displayName).isEqualTo(displayName)
    }
}

private fun successJson(
    latLng: LatLng,
    displayName: String
): String = """
        {
          "place_id": 123,
          "licence": "Data © OpenStreetMap contributors, ODbL 1.0. https://osm.org/copyright",
          "osm_type": "node",
          "osm_id": 123,
          "lat": "${latLng.latitude}",
          "lon": "${latLng.longitude}",
          "display_name": "$displayName",
          "address": {
            "country": "India"
          },
          "boundingbox": [
            "-5.05E-5",
            "4.95E-5",
            "-4.95E-5",
            "5.05E-5"
          ]
        }
    """.trimIndent()
