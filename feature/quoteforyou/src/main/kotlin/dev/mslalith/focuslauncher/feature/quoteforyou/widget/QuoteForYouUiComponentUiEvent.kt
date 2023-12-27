package dev.mslalith.focuslauncher.feature.quoteforyou.widget

import com.slack.circuit.runtime.CircuitUiEvent

sealed interface QuoteForYouUiComponentUiEvent : CircuitUiEvent {
    data object FetchNextQuote : QuoteForYouUiComponentUiEvent
}
