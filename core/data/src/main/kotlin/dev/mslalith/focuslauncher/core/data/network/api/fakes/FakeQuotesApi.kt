package dev.mslalith.focuslauncher.core.data.network.api.fakes

import dev.mslalith.focuslauncher.core.data.network.api.QuotesApi
import dev.mslalith.focuslauncher.core.data.network.entities.QuotesApiResponse
import dev.mslalith.focuslauncher.core.data.utils.dummyQuoteResponseFor

internal class FakeQuotesApi : QuotesApi {
    override suspend fun getQuotes(page: Int, limit: Int): QuotesApiResponse {
        val pageOffset = (page - 1) * limit
        return QuotesApiResponse(
            count = limit,
            totalCount = 100,
            page = page,
            totalPages = 20,
            lastItemIndex = 9,
            results = List(size = limit) { dummyQuoteResponseFor(index = pageOffset + it) }
        )
    }
}
