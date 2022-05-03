package dev.mslalith.focuslauncher

import dev.mslalith.focuslauncher.data.network.api.QuotesApi
import dev.mslalith.focuslauncher.data.network.entities.QuoteResponse
import dev.mslalith.focuslauncher.data.network.entities.QuotesApiResponse

class FakeQuotesApi : QuotesApi {
    override suspend fun getQuotes(page: Int, limit: Int): QuotesApiResponse {
        val quoteResponseList = buildList {
            val quotes = TestQuotes.buildQuoteResponses(page, limit)
            addAll(quotes)
        }
        return QuotesApiResponse(
            count = quoteResponseList.size,
            totalCount = quoteResponseList.size,
            page = page,
            totalPages = 5,
            lastItemIndex = 5,
            results = quoteResponseList
        )
    }
}

private object TestQuotes {

    fun buildQuoteResponses(page: Int, limit: Int) = buildList {
        val start = (page - 1) * limit
        repeat(limit) {
            add(buildSingleQuoteResponse(start + it))
        }
    }

    fun buildSingleQuoteResponse(index: Int) = QuoteResponse(
        id = index.toString(),
        tags = listOf("tag_1", "tag_2"),
        quote = "Quote #$index",
        author = "Author #$index",
        authorSlug = "author_$index",
        length = index
    )
}
