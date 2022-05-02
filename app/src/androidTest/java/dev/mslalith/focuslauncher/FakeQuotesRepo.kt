package dev.mslalith.focuslauncher

import dev.mslalith.focuslauncher.data.database.entities.Quote
import dev.mslalith.focuslauncher.data.models.Outcome
import dev.mslalith.focuslauncher.data.repository.interfaces.QuotesRepo
import dev.mslalith.focuslauncher.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeQuotesRepo : QuotesRepo {
    private val _currentQuoteStateFlow = MutableStateFlow<Outcome<Quote>>(Outcome.None)
    override val currentQuoteStateFlow: StateFlow<Outcome<Quote>>
        get() = _currentQuoteStateFlow

    private val _isFetchingQuotesStateFlow = MutableStateFlow(false)
    override val isFetchingQuotesStateFlow: StateFlow<Boolean>
        get() = _isFetchingQuotesStateFlow

    private val quotesApi = FakeQuotesApi()
    private val quoteList = mutableListOf<Quote>()

    var randomIndex = 0

    fun getQuoteAt(index: Int) = quoteList[index]

    override suspend fun nextRandomQuote() {
        _currentQuoteStateFlow.value = Outcome.Success(quoteList[randomIndex])
    }

    override suspend fun fetchQuotes(maxPages: Int) {
        _isFetchingQuotesStateFlow.value = true
        val quotes = buildList {
            repeat(maxPages) { page ->
                val list = quotesApi.getQuotes(page + 1).results.map { it.toQuote() }
                addAll(list)
            }
        }
        quoteList.addAll(quotes)
        _isFetchingQuotesStateFlow.value = false
    }

    override suspend fun hasQuotesReachedLimit(): Boolean = quoteList.size >= Constants.Defaults.QUOTES_LIMIT

    override suspend fun quotesSize(): Int = quoteList.size
}
