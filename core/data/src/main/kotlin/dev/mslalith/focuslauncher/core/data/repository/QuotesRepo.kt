package dev.mslalith.focuslauncher.core.data.repository

import dev.mslalith.focuslauncher.core.common.State
import dev.mslalith.focuslauncher.core.model.Quote
import kotlin.random.Random
import kotlinx.coroutines.flow.StateFlow

interface QuotesRepo {
    val currentQuoteStateFlow: StateFlow<State<Quote>>
    val isFetchingQuotesStateFlow: StateFlow<Boolean>

    fun getRandomIndex(size: Int): Int = Random.nextInt(size)

    suspend fun nextRandomQuote()
    suspend fun fetchQuotes(maxPages: Int)
    suspend fun addInitialQuotesIfNeeded()
    suspend fun hasQuotesReachedLimit(): Boolean
    suspend fun quotesSize(): Int
}
