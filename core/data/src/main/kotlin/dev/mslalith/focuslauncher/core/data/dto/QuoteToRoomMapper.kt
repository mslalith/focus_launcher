package dev.mslalith.focuslauncher.core.data.dto

import dev.mslalith.focuslauncher.core.data.database.dao.QuotesDao
import dev.mslalith.focuslauncher.core.data.database.entities.QuoteRoom
import dev.mslalith.focuslauncher.core.model.Quote
import javax.inject.Inject

internal class QuoteToRoomMapper @Inject constructor(
    private val quotesDao: QuotesDao
) : Mapper<QuoteRoom, Quote> {
    override fun fromEntity(data: QuoteRoom) = Quote(
        id = data.id,
        quote = data.quote,
        author = data.author
    )

    override fun toEntity(data: Quote): QuoteRoom {
        return quotesDao.getQuoteBySync(data.id)
            ?: throw IllegalStateException("Quote with ${data.id} was not found in Database")
    }
}
