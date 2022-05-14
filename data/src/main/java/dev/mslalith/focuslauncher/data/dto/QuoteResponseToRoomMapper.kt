package dev.mslalith.focuslauncher.data.dto

import dev.mslalith.focuslauncher.data.database.entities.QuoteRoom
import dev.mslalith.focuslauncher.data.network.entities.QuoteResponse

class QuoteResponseToRoomMapper : Mapper<QuoteResponse, QuoteRoom> {
    override fun fromEntity(data: QuoteResponse) = QuoteRoom(
        id = data.id,
        quote = data.quote,
        author = data.author,
        authorSlug = data.authorSlug,
        length = data.length,
        tags = data.tags
    )

    override fun toEntity(data: QuoteRoom) = QuoteResponse(
        id = data.id,
        quote = data.quote,
        author = data.author,
        authorSlug = data.authorSlug,
        length = data.length,
        tags = data.tags
    )
}
