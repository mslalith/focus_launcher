package dev.mslalith.focuslauncher.data.respository.interfaces

import dev.mslalith.focuslauncher.data.database.entities.Quote
import dev.mslalith.focuslauncher.data.models.Outcome
import kotlinx.coroutines.flow.StateFlow

interface QuotesRepo {
    val currentQuoteStateStateFlow: StateFlow<Outcome<Quote>>
    val isFetchingQuotesStateStateFlow: StateFlow<Boolean>

    suspend fun nextRandomQuote()
    suspend fun fetchQuotes(maxPages: Int = 2)
    suspend fun hasQuotesReachedLimit(): Boolean
    suspend fun quotesSize(): Int
}
