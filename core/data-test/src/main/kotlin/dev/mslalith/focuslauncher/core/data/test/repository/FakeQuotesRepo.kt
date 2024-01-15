package dev.mslalith.focuslauncher.core.data.test.repository

import dev.mslalith.focuslauncher.core.common.model.State
import dev.mslalith.focuslauncher.core.data.repository.QuotesRepo
import dev.mslalith.focuslauncher.core.data.utils.dummyQuoteFor
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.QUOTES_LIMIT
import dev.mslalith.focuslauncher.core.model.Quote
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class FakeQuotesRepo : QuotesRepo {

    var nextQuoteIndex = 0

    private val allQuotes = mutableSetOf<Quote>()

    private val _currentQuoteStateFlow = MutableStateFlow<State<Quote>>(value = State.Initial)
    override val currentQuoteStateFlow: StateFlow<State<Quote>> = _currentQuoteStateFlow

    private val _isFetchingQuotesStateFlow = MutableStateFlow(value = false)
    override val isFetchingQuotesStateFlow: StateFlow<Boolean> = _isFetchingQuotesStateFlow

    override suspend fun nextRandomQuote() {
        if (quotesSize() == 0) addInitialQuotes()

        _currentQuoteStateFlow.update {
            if (allQuotes.isEmpty()) State.Initial else {
                State.Success(value = allQuotes.elementAt(nextQuoteIndex))
            }
        }
    }

    override suspend fun fetchQuotes(maxPages: Int) {
        _isFetchingQuotesStateFlow.value = true
        delay(timeMillis = 3000L)

        val limit = 10
        val start = limit * maxPages
        val end = start + limit
        (start..end).forEach { allQuotes.add(dummyQuoteFor(index = it)) }

        _isFetchingQuotesStateFlow.value = false
    }

    override suspend fun addInitialQuotesIfNeeded() {
        if (quotesSize() == 0) addInitialQuotes()
    }

    override suspend fun hasQuotesReachedLimit(): Boolean = quotesSize() >= QUOTES_LIMIT

    override suspend fun quotesSize(): Int = allQuotes.size

    private fun addInitialQuotes() {
        (1..9).forEach { allQuotes.add(dummyQuoteFor(index = it)) }
    }
}
