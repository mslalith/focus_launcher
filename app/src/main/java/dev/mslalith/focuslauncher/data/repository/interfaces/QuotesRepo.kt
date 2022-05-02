package dev.mslalith.focuslauncher.data.repository.interfaces

import dev.mslalith.focuslauncher.data.database.entities.Quote
import dev.mslalith.focuslauncher.data.models.Outcome
import kotlinx.coroutines.flow.StateFlow

interface QuotesRepo {
    val currentQuoteStateFlow: StateFlow<Outcome<Quote>>
    val isFetchingQuotesStateFlow: StateFlow<Boolean>

    suspend fun nextRandomQuote()
    suspend fun fetchQuotes(maxPages: Int = 2)
    suspend fun hasQuotesReachedLimit(): Boolean
    suspend fun quotesSize(): Int
}
