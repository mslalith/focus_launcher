package dev.mslalith.focuslauncher.feature.quoteforyou.bottomsheet.quotewidgetsettings

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import dev.mslalith.focuslauncher.core.common.model.State
import dev.mslalith.focuslauncher.core.model.Quote

data class QuoteWidgetSettingsBottomSheetState(
    val showQuotes: Boolean,
    val isFetchingQuotes: Boolean,
    val currentQuote: State<Quote>,
    val eventSink: (QuoteWidgetSettingsBottomSheetUiEvent) -> Unit
) : CircuitUiState

sealed interface QuoteWidgetSettingsBottomSheetUiEvent : CircuitUiEvent {
    data object ToggleShowQuoteWidget : QuoteWidgetSettingsBottomSheetUiEvent
    data object FetchQuoteWidget : QuoteWidgetSettingsBottomSheetUiEvent
    data object FetchNextQuote : QuoteWidgetSettingsBottomSheetUiEvent
}
