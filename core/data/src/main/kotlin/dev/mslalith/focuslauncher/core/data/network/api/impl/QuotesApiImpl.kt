package dev.mslalith.focuslauncher.core.data.network.api.impl

import dev.mslalith.focuslauncher.core.data.network.api.QuotesApi
import dev.mslalith.focuslauncher.core.data.network.entities.QuotesApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.DoubleReceiveException
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.JsonConvertException
import javax.inject.Inject

internal class QuotesApiImpl @Inject constructor(
    private val httpClient: HttpClient
) : QuotesApi {

    override suspend fun getQuotes(page: Int, limit: Int): Result<QuotesApiResponse> = try {
        Result.success(
            value = httpClient.get(urlString = "https://api.quotable.io/quotes") {
                parameter(key = "page", value = page)
                parameter(key = "limit", value = limit)
            }.body()
        )
    } catch (ex: JsonConvertException) {
        Result.failure(exception = ex)
    } catch (ex: DoubleReceiveException) {
        Result.failure(exception = ex)
    } catch (ex: HttpRequestTimeoutException) {
        Result.failure(exception = ex)
    }
}
