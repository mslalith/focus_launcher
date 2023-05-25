package dev.mslalith.focuslauncher.core.data.network.api

import dev.mslalith.focuslauncher.core.data.network.entities.QuotesApiResponse
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.QUOTES_LIMIT_PER_PAGE

internal interface QuotesApi {
    suspend fun getQuotes(
        page: Int = 1,
        limit: Int = QUOTES_LIMIT_PER_PAGE
    ): Result<QuotesApiResponse>
}
