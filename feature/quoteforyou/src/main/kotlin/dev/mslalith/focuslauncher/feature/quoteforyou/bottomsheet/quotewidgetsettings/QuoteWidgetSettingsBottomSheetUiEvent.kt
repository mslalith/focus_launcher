package dev.mslalith.focuslauncher.feature.quoteforyou.bottomsheet.quotewidgetsettings

import com.slack.circuit.runtime.CircuitUiEvent

sealed interface QuoteWidgetSettingsBottomSheetUiEvent : CircuitUiEvent {
    data object ToggleShowQuoteWidget : QuoteWidgetSettingsBottomSheetUiEvent
    data object FetchQuoteWidget : QuoteWidgetSettingsBottomSheetUiEvent
}
