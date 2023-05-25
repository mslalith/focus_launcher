package dev.mslalith.focuslauncher.core.testing

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.junit.Before

data class MockHandler(
    val url: String,
    val trigger: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData
)

open class KtorApiTest : CoroutineTest() {

    private val mockHandlers = mutableListOf<MockHandler>()

    private val mockEngine = MockEngine { request ->
        val url = request.url.toString()
        val handler = mockHandlers.firstOrNull { it.url == url || url.startsWith(prefix = it.url) }
        return@MockEngine handler?.trigger?.invoke(this, request) ?: throw IllegalStateException("No MockHandler provided for $url")
    }

    protected val client = HttpClient(engine = mockEngine) {
        install(plugin = ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                }
            )
        }

        val timeout = 15_000L
        install(plugin = HttpTimeout) {
            requestTimeoutMillis = timeout
            connectTimeoutMillis = timeout
            socketTimeoutMillis = timeout
        }
    }

    @Before
    fun setup() {
        clearHandlers()
    }

    protected fun clearHandlers() = mockHandlers.clear()

    protected fun onRequestTo(
        url: String,
        block: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData
    ) {
        mockHandlers.add(
            element = MockHandler(
                url = url,
                trigger = block
            )
        )
    }

    protected fun MockRequestHandleScope.successResponse(content: String): HttpResponseData = response(content = content)

    protected fun MockRequestHandleScope.failureResponse(
        content: String,
        status: HttpStatusCode = HttpStatusCode.NotFound
    ): HttpResponseData = response(
        content = content,
        status = status
    )

    protected fun MockRequestHandleScope.response(
        content: String,
        status: HttpStatusCode = HttpStatusCode.OK,
        headers: Headers = headersOf(HttpHeaders.ContentType, "application/json")
    ): HttpResponseData = respond(
        content = content,
        status = status,
        headers = headers
    )
}
