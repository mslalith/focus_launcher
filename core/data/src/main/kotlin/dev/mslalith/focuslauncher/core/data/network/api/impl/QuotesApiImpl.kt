package dev.mslalith.focuslauncher.core.data.network.api.impl

import dev.mslalith.focuslauncher.core.data.network.api.QuotesApi
import dev.mslalith.focuslauncher.core.data.network.entities.QuotesApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

internal class QuotesApiImpl @Inject constructor(
    private val httpClient: HttpClient
) : QuotesApi {

    private val baseUrl = "https://api.quotable.io"

    override suspend fun getQuotes(page: Int, limit: Int): QuotesApiResponse {
        return httpClient.get("$baseUrl/quotes") {
            parameter("page", page)
            parameter("limit", limit)
        }.body()
    }
}
