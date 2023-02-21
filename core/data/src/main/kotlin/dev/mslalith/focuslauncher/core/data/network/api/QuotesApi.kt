package dev.mslalith.focuslauncher.core.data.network.api

import dev.mslalith.focuslauncher.core.data.network.entities.QuotesApiResponse
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.QUOTES_LIMIT_PER_PAGE
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

internal interface QuotesApi {
    suspend fun getQuotes(
        page: Int = 1,
        limit: Int = QUOTES_LIMIT_PER_PAGE
    ): QuotesApiResponse
}

internal class QuotesApiKtorImpl @Inject constructor(
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
