package dev.mslalith.focuslauncher.data.network.api

import dev.mslalith.focuslauncher.data.network.entities.QuotesApiResponse
import dev.mslalith.focuslauncher.data.utils.Constants
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import javax.inject.Inject

interface QuotesApi {
    suspend fun getQuotes(
        page: Int = 1,
        limit: Int = Constants.Defaults.QUOTES_LIMIT_PER_PAGE,
    ): QuotesApiResponse
}

internal class QuotesApiKtorImpl @Inject constructor(
    private val httpClient: HttpClient,
) : QuotesApi {

    private val baseUrl = "https://api.quotable.io"

    override suspend fun getQuotes(page: Int, limit: Int): QuotesApiResponse {
        return httpClient.get {
            url("$baseUrl/quotes?page=$page&limit=$limit")
        }
    }
}
