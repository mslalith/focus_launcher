package dev.mslalith.focuslauncher.data.network.api

import dev.mslalith.focuslauncher.data.network.entities.QuotesApiResponse
import dev.mslalith.focuslauncher.data.utils.Constants
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.url
import kotlinx.serialization.json.Json

interface QuotesApiKtor {
    suspend fun getQuotes(
        page: Int = 1,
        limit: Int = Constants.Defaults.QUOTES_LIMIT_PER_PAGE,
    ): QuotesApiResponse

    companion object {
        fun create(): QuotesApiKtor = QuoteApiKtorImpl(httpClient = ktorClient)
    }
}

private val ktorClient by lazy {
    HttpClient(Android) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = false
                }
            )
        }

        val timeout = 15_000L
        install(HttpTimeout) {
            requestTimeoutMillis = timeout
            connectTimeoutMillis = timeout
            socketTimeoutMillis = timeout
        }
    }
}

private class QuoteApiKtorImpl(
    private val httpClient: HttpClient
) : QuotesApiKtor {

    private val baseUrl = "https://api.quotable.io"

    override suspend fun getQuotes(page: Int, limit: Int): QuotesApiResponse {
        return httpClient.get {
            url("$baseUrl/quotes?page=$page&limit=$limit")
        }
    }
}
