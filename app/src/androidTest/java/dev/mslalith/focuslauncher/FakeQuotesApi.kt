package dev.mslalith.focuslauncher

import dev.mslalith.focuslauncher.data.api.QuotesApi
import dev.mslalith.focuslauncher.data.database.entities.Quote
import dev.mslalith.focuslauncher.data.models.QuoteResponse
import dev.mslalith.focuslauncher.data.models.QuotesApiResponse

class FakeQuotesApi : QuotesApi {
    override suspend fun getQuotes(page: Int, limit: Int): QuotesApiResponse {
        val quoteResponseList = buildList {
            val quotes = TestQuotes.buildQuotes(page, limit).map(TestQuotes::toQuoteResponse)
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

    fun buildQuotes(page: Int, limit: Int) = buildList {
        val start = (page - 1) * limit
        repeat(limit) {
            add(buildSingleQuote(start + it))
        }
    }

    fun buildSingleQuote(index: Int) = Quote(
        id = index.toString(),
        tags = listOf("tag_1", "tag_2"),
        quote = "Quote #$index",
        author = "Author #$index",
        authorSlug = "author_$index",
        length = index
    )

    fun toQuoteResponse(quote: Quote) = QuoteResponse(
        id = quote.id,
        quote = quote.quote,
        author = quote.author,
        authorSlug = quote.authorSlug,
        length = quote.length,
        tags = quote.tags
    )
}
