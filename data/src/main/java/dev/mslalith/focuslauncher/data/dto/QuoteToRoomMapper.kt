package dev.mslalith.focuslauncher.data.dto

import dev.mslalith.focuslauncher.data.database.dao.QuotesDao
import dev.mslalith.focuslauncher.data.database.entities.QuoteRoom
import dev.mslalith.focuslauncher.data.model.Quote
import javax.inject.Inject

class QuoteToRoomMapper @Inject constructor(
    private val quotesDao: QuotesDao,
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
