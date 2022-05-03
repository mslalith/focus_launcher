package dev.mslalith.focuslauncher.data.repository

import androidx.annotation.VisibleForTesting
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.mslalith.focuslauncher.data.database.dao.QuotesDao
import dev.mslalith.focuslauncher.data.database.entities.QuoteRoom
import dev.mslalith.focuslauncher.data.model.Outcome
import dev.mslalith.focuslauncher.data.model.Quote
import dev.mslalith.focuslauncher.data.network.api.QuotesApi
import dev.mslalith.focuslauncher.data.network.entities.QuoteResponse
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.QUOTES_LIMIT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuotesRepo @Inject constructor(
    private val quotesApi: QuotesApi,
    private val quotesDao: QuotesDao
) {
    private val _currentQuoteStateFlow = MutableStateFlow<Outcome<Quote>>(Outcome.None)
    val currentQuoteStateFlow: StateFlow<Outcome<Quote>>
        get() = _currentQuoteStateFlow

    private val _isFetchingQuotesStateFlow = MutableStateFlow(false)
    val isFetchingQuotesStateFlow: StateFlow<Boolean>
        get() = _isFetchingQuotesStateFlow

    suspend fun nextRandomQuote() {
        if (quotesSize() == 0) addInitialQuotes()

        val quoteOutcome = quotesDao.getQuotes().let {
            if (it.isEmpty()) Outcome.None
            else Outcome.Success(it.random().toQuote())
        }
        _currentQuoteStateFlow.value = quoteOutcome
    }

    @VisibleForTesting
    suspend fun nextRandomQuoteTest(index: Int): Outcome<Quote> {
        if (quotesSize() == 0) addInitialQuotes()

        val quoteOutcome = quotesDao.getQuotes().let {
            if (it.isEmpty()) Outcome.None
            else Outcome.Success(it[index].toQuote())
        }
        _currentQuoteStateFlow.value = quoteOutcome
        return quoteOutcome
    }

    suspend fun fetchQuotes(maxPages: Int) {
        _isFetchingQuotesStateFlow.value = true
        repeat(maxPages) {
            withContext(Dispatchers.IO) { fetchPageQuotes(page = it + 1) }
        }
        _isFetchingQuotesStateFlow.value = false
    }

    private suspend fun fetchPageQuotes(page: Int) {
        val quotesApiResponse = quotesApi.getQuotes(page = page)
        val quotes = quotesApiResponse.results.map { it.toQuoteRoom() }
        addAllQuotes(quotes)
    }

    private suspend fun addInitialQuotes() {
        val quotesListType = object : TypeToken<List<QuoteResponse>>() {}.type
        val initialQuoteResponses = Gson().fromJson<List<QuoteResponse>>(INITIAL_QUOTES_JSON, quotesListType)
        val initialQuotes = initialQuoteResponses.map { it.toQuoteRoom() }
        addAllQuotes(initialQuotes)
        nextRandomQuote()
    }

    private suspend fun addAllQuotes(quotes: List<QuoteRoom>) = quotesDao.addQuotes(quotes)

    suspend fun hasQuotesReachedLimit() = quotesSize() >= QUOTES_LIMIT
    suspend fun quotesSize() = quotesDao.getQuotesSize()

    companion object {
        private const val INITIAL_QUOTES_JSON = """
            [
                {
                  "_id": "vuGBuD1oaev3",
                  "tags": ["famous-quotes", "wisdom"],
                  "content": "Do not go where the path may lead, go instead where there is no path and leave a trail.",
                  "author": "Ralph Waldo Emerson",
                  "authorSlug": "ralph-waldo-emerson",
                  "length": 87
                },
                {
                  "_id": "f1aZRYvKb7Ga",
                  "tags": ["famous-quotes", "life", "wisdom"],
                  "content": "You have to do your own growing no matter how tall your grandfather was.",
                  "author": "Abraham Lincoln",
                  "authorSlug": "abraham-lincoln",
                  "length": 72
                },
                {
                  "_id": "TcJ1vf7DaqhU",
                  "tags": ["famous-quotes", "wisdom"],
                  "content": "It's not what you look at that matters, it's what you see.",
                  "author": "Henry David Thoreau",
                  "authorSlug": "henry-david-thoreau",
                  "length": 58
                },
                {
                  "_id": "2C-BAEVx44Os",
                  "tags": ["famous-quotes", "wisdom"],
                  "content": "I walk slowly, but I never walk backward.", 
                  "author": "Abraham Lincoln",
                  "authorSlug": "abraham-lincoln",
                  "length": 41
                },
                {
                  "_id": "lFPVQ6WAR4",
                  "tags": ["wisdom"],
                  "content": "True wisdom comes to each of us when we realize how little we understand about life, ourselves, and the world around us.",
                  "author": "Isocrates",
                  "authorSlug": "isocrates",
                  "length": 120
                },
                {
                  "_id": "dYKQx6tn7k-y",
                  "tags": ["famous-quotes", "wisdom"],
                  "content": "Work like you don't need the money. Love like you've never been hurt. Dance like nobody's watching.",
                  "author": "Satchel Paige",
                  "authorSlug": "satchel-paige",
                  "length": 99
                },
                {
                  "_id": "mvAZepcjue",
                  "tags": ["wisdom"],
                  "content": "Start with what is right rather than what is acceptable.",
                  "author": "Franz Kafka",
                  "authorSlug": "franz-kafka",
                  "length": 56
                },
                {
                  "_id": "n-sqGiK54E",
                  "tags": ["wisdom"],
                  "content": "It is the province of knowledge to speak, and it is the privilege of wisdom to listen.",
                  "author": "Oliver Wendell Holmes Jr.",
                  "authorSlug": "oliver-wendell-holmes-jr",
                  "length": 86
                },
                {
                  "_id": "TGjbJGdKmhb",
                  "tags": ["wisdom"],
                  "content": "If you only have a hammer, you tend to see every problem as a nail.",
                  "author": "Abraham Maslow",
                  "authorSlug": "abraham-maslow",
                  "length": 67
                }
            ]
        """
    }
}
