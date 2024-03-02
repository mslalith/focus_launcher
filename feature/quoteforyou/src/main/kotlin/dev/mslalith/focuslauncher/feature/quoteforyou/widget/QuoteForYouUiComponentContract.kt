package dev.mslalith.focuslauncher.feature.quoteforyou.widget

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import dev.mslalith.focuslauncher.core.common.model.State
import dev.mslalith.focuslauncher.core.model.Quote

data class QuoteForYouUiComponentState(
    val showQuotes: Boolean,
    val currentQuote: State<Quote>,
    val eventSink: (QuoteForYouUiComponentUiEvent) -> Unit
) : CircuitUiState

sealed interface QuoteForYouUiComponentUiEvent : CircuitUiEvent {
    data object FetchNextQuote : QuoteForYouUiComponentUiEvent
}
