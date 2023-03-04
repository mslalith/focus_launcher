package dev.mslalith.focuslauncher.core.data.utils

import dev.mslalith.focuslauncher.core.data.network.entities.QuoteResponse
import dev.mslalith.focuslauncher.core.model.Quote

internal fun dummyQuoteResponseFor(index: Int) = QuoteResponse(
    id = "ID #$index",
    quote = "Quote #$index",
    author = "Author #$index",
    authorSlug = "Author Slug #$index",
    tags = listOf(),
    length = 20
)

internal fun dummyQuoteFor(index: Int) = Quote(
    id = "ID #$index",
    quote = "Quote #$index",
    author = "Author #$index"
)
