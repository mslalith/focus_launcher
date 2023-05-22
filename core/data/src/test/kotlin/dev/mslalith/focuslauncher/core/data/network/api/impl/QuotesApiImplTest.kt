package dev.mslalith.focuslauncher.core.data.network.api.impl

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.core.data.network.api.QuotesApi
import dev.mslalith.focuslauncher.core.data.network.api.fakes.FakeQuotesApi
import dev.mslalith.focuslauncher.core.testing.CoroutineTest
import org.junit.Test

internal class QuotesApiImplTest : CoroutineTest() {

    private val api: QuotesApi = FakeQuotesApi()

    @Test
    fun `01 - when requesting with 10 per page, API must return exactly 10`() = runCoroutineTest {
        val response = api.getQuotes(page = 1, limit = 10)
        assertThat(response.results.size).isEqualTo(10)
    }
}
