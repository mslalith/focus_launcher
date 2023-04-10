package dev.mslalith.focuslauncher.core.data.dto

import dev.mslalith.focuslauncher.core.data.database.entities.QuoteRoom
import dev.mslalith.focuslauncher.core.data.network.entities.QuoteResponse
import dev.mslalith.focuslauncher.core.model.Quote

internal fun QuoteResponse.toQuoteRoom(): QuoteRoom = QuoteRoom(
    id = id,
    quote = quote,
    author = author,
    authorSlug = authorSlug,
    length = length,
    tags = tags
)

internal fun QuoteRoom.toQuote(): Quote = Quote(
    id = id,
    quote = quote,
    author = author
)
