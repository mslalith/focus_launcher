package dev.mslalith.focuslauncher.feature.quoteforyou

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.common.State
import dev.mslalith.focuslauncher.core.ui.extensions.launchInIO
import dev.mslalith.focuslauncher.core.ui.extensions.withinScope
import dev.mslalith.focuslauncher.data.repository.QuotesRepo
import dev.mslalith.focuslauncher.data.repository.settings.QuotesSettingsRepo
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.Quotes.DEFAULT_SHOW_QUOTES
import dev.mslalith.focuslauncher.feature.quoteforyou.model.QuoteForYouState
import javax.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf

@HiltViewModel
class QuoteForYouViewModel @Inject constructor(
    private val quotesRepo: QuotesRepo,
    quotesSettingsRepo: QuotesSettingsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    init {
        appCoroutineDispatcher.launchInIO {
            quotesRepo.addInitialQuotesIfNeeded()
            quotesRepo.nextRandomQuote()
        }
    }

    private val defaultQuoteForYouState = QuoteForYouState(
        showQuotes = DEFAULT_SHOW_QUOTES,
        currentQuote = State.Initial
    )

    val quoteForYouState = flowOf(defaultQuoteForYouState)
        .combine(quotesSettingsRepo.showQuotesFlow) { state, showQuotes ->
            state.copy(showQuotes = showQuotes)
        }.combine(quotesRepo.currentQuoteStateFlow) { state, currentQuote ->
            state.copy(currentQuote = currentQuote)
        }.withinScope(initialValue = defaultQuoteForYouState)

    fun nextRandomQuote() {
        appCoroutineDispatcher.launchInIO {
            quotesRepo.nextRandomQuote()
        }
    }
}