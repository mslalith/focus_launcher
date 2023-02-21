package dev.mslalith.focuslauncher.core.data.dto

import dev.mslalith.focuslauncher.core.data.database.entities.QuoteRoom
import dev.mslalith.focuslauncher.core.data.network.entities.QuoteResponse

internal class QuoteResponseToRoomMapper : Mapper<QuoteResponse, QuoteRoom> {
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
