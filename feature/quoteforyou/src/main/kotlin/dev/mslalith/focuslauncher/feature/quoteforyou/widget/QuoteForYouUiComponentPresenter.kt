package dev.mslalith.focuslauncher.feature.quoteforyou.widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.QuotesRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.QuotesSettingsRepo
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.Quotes.DEFAULT_SHOW_QUOTES
import dev.mslalith.focuslauncher.core.screens.QuoteForYouUiComponentScreen
import dev.mslalith.focuslauncher.feature.quoteforyou.widget.QuoteForYouUiComponentUiEvent.FetchNextQuote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@CircuitInject(QuoteForYouUiComponentScreen::class, SingletonComponent::class)
class QuoteForYouUiComponentPresenter @Inject constructor(
    private val quotesRepo: QuotesRepo,
    private val quotesSettingsRepo: QuotesSettingsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
): Presenter<QuoteForYouUiComponentState> {

    @Composable
    override fun present(): QuoteForYouUiComponentState {
        val scope = rememberCoroutineScope()

        val showQuotes by quotesSettingsRepo.showQuotesFlow.collectAsRetainedState(initial = DEFAULT_SHOW_QUOTES)
        val currentQuote by quotesRepo.currentQuoteStateFlow.collectAsRetainedState()

        return QuoteForYouUiComponentState(
            showQuotes = showQuotes,
            currentQuote = currentQuote
        ) {
            when (it) {
                FetchNextQuote -> scope.nextRandomQuote()
            }
        }
    }

    private fun CoroutineScope.nextRandomQuote() {
        launch(appCoroutineDispatcher.io) {
            quotesRepo.nextRandomQuote()
        }
    }
}
