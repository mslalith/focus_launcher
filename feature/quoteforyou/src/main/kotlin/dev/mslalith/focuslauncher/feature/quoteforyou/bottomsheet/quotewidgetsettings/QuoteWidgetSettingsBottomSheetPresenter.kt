package dev.mslalith.focuslauncher.feature.quoteforyou.bottomsheet.quotewidgetsettings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.common.network.NetworkMonitor
import dev.mslalith.focuslauncher.core.data.repository.QuotesRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.QuotesSettingsRepo
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.Quotes.DEFAULT_SHOW_QUOTES
import dev.mslalith.focuslauncher.core.screens.QuoteWidgetSettingsBottomSheetScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@CircuitInject(QuoteWidgetSettingsBottomSheetScreen::class, SingletonComponent::class)
class QuoteWidgetSettingsBottomSheetPresenter @Inject constructor(
    private val quotesRepo: QuotesRepo,
    private val quotesSettingsRepo: QuotesSettingsRepo,
    private val networkMonitor: NetworkMonitor,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : Presenter<QuoteWidgetSettingsBottomSheetState> {

    @Composable
    override fun present(): QuoteWidgetSettingsBottomSheetState {
        val scope = rememberCoroutineScope()

        val showQuotes by quotesSettingsRepo.showQuotesFlow.collectAsState(initial = DEFAULT_SHOW_QUOTES)
        val isFetchingQuotes by quotesRepo.isFetchingQuotesStateFlow.collectAsState()
        val currentQuote by quotesRepo.currentQuoteStateFlow.collectAsState()

        LaunchedEffect(key1 = Unit) {
            quotesRepo.addInitialQuotesIfNeeded()
            quotesRepo.nextRandomQuote()
        }

        return QuoteWidgetSettingsBottomSheetState(
            showQuotes = showQuotes,
            isFetchingQuotes = isFetchingQuotes,
            currentQuote = currentQuote
        ) {
            when (it) {
                QuoteWidgetSettingsBottomSheetUiEvent.ToggleShowQuoteWidget -> scope.toggleShowQuotes()
                QuoteWidgetSettingsBottomSheetUiEvent.FetchQuoteWidget -> scope.fetchQuotesIfRequired()
            }
        }
    }

    fun CoroutineScope.toggleShowQuotes() {
        launch(appCoroutineDispatcher.io) {
            quotesSettingsRepo.toggleShowQuotes()
        }
    }

    fun CoroutineScope.fetchQuotesIfRequired() {
        if (!networkMonitor.isCurrentlyConnected()) return

        launch(appCoroutineDispatcher.io) {
            if (quotesRepo.hasQuotesReachedLimit()) return@launch
            if (quotesRepo.isFetchingQuotesStateFlow.value) return@launch

            quotesRepo.fetchQuotes(maxPages = 2)
        }
    }
}
