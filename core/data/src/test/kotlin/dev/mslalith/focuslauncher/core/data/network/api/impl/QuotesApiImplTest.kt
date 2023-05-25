package dev.mslalith.focuslauncher.core.data.network.api.impl

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.core.data.network.entities.QuoteResponse
import dev.mslalith.focuslauncher.core.data.network.entities.QuotesApiResponse
import dev.mslalith.focuslauncher.core.testing.KtorApiTest
import io.ktor.client.call.DoubleReceiveException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.serialization.JsonConvertException
import io.mockk.mockk
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
internal class QuotesApiImplTest : KtorApiTest() {

    private val quotesApi = QuotesApiImpl(httpClient = client)
    private val url = "https://api.quotable.io/quotes"

    @Test
    fun `01 - when requesting with 10 per page, API must return exactly 10`() = runCoroutineTest {
        onRequestTo(url = url) {
            successResponse(content = successJson(limit = 10))
        }

        quotesApi.getQuotes(
            page = 1,
            limit = 10
        ).assertWith(limit = 10)
    }

    @Test
    fun `02- when requesting with 20 per page, API must return exactly 20`() = runCoroutineTest {
        onRequestTo(url = url) {
            successResponse(content = successJson(limit = 20))
        }

        quotesApi.getQuotes(
            page = 1,
            limit = 20
        ).assertWith(limit = 20)
    }

    @Test
    fun `03 - verify JsonConvertException`() = runCoroutineTest {
        onRequestTo(url = url) {
            throw JsonConvertException(message = "Test exception")
        }

        quotesApi.getQuotes(page = 1, limit = 10).assertException<JsonConvertException>()
    }

    @Test
    fun `04 - verify DoubleReceiveException`() = runCoroutineTest {
        onRequestTo(url = url) {
            throw DoubleReceiveException(call = mockk())
        }

        quotesApi.getQuotes(page = 1, limit = 10).assertException<DoubleReceiveException>()
    }

    @Test
    fun `05 - verify HttpRequestTimeoutException`() = runCoroutineTest {
        onRequestTo(url = url) {
            throw HttpRequestTimeoutException(url = "", timeoutMillis = null)
        }

        quotesApi.getQuotes(page = 1, limit = 10).assertException<HttpRequestTimeoutException>()
    }
}

private inline fun <reified T : Exception> Result<QuotesApiResponse>.assertException() {
    exceptionOrNull().apply {
        assertThat(this).isNotNull()
        assertThat(this).isInstanceOf(T::class.java)
    }
}

private fun Result<QuotesApiResponse>.assertWith(
    limit: Int
) {
    getOrNull().apply {
        assertThat(this).isNotNull()
        assertThat(this?.count).isEqualTo(limit)
        assertThat(this?.results?.size).isEqualTo(limit)
        repeat(times = limit) {
            val expected = Json.decodeFromString<QuoteResponse>(string = singleQuote(index = it).toString())
            assertThat(this?.results?.get(index = it)).isEqualTo(expected)
        }
    }
}

private fun successJson(
    limit: Int
): String = buildJsonObject {
    put("count", limit)
    put("totalCount", 20)
    put("page", 1)
    put("totalPages", 50)
    put("lastItemIndex", 1)
    putJsonArray("results") {
        repeat(times = limit) {
            add(singleQuote(index = it))
        }
    }
}.toString()

private fun singleQuote(index: Int): JsonObject = buildJsonObject {
    put("_id", "test_id")
    put("author", "Author #$index")
    put("content", "Content #$index")
    putJsonArray("tags") { add("tag_$index") }
    put("authorSlug", "slug_$index")
    put("length", 38)
}
