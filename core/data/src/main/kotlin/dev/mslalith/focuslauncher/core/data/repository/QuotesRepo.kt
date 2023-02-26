package dev.mslalith.focuslauncher.core.data.repository

import dev.mslalith.focuslauncher.core.common.State
import dev.mslalith.focuslauncher.core.model.Quote
import kotlinx.coroutines.flow.StateFlow

interface QuotesRepo {
    val currentQuoteStateFlow: StateFlow<State<Quote>>
    val isFetchingQuotesStateFlow: StateFlow<Boolean>

    suspend fun nextRandomQuote()
    suspend fun fetchQuotes(maxPages: Int)
    suspend fun addInitialQuotesIfNeeded()
    suspend fun hasQuotesReachedLimit(): Boolean
    suspend fun quotesSize(): Int
}
