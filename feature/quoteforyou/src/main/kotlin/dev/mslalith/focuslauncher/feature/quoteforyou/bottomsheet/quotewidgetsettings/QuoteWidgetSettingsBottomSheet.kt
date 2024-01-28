package dev.mslalith.focuslauncher.feature.quoteforyou.bottomsheet.quotewidgetsettings

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.screens.QuoteWidgetSettingsBottomSheetScreen
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.settings.SettingsLoadableItem
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableSwitchItem
import dev.mslalith.focuslauncher.feature.quoteforyou.R
import dev.mslalith.focuslauncher.feature.quoteforyou.settings.PreviewQuotes
import dev.mslalith.focuslauncher.feature.quoteforyou.widget.QuoteForYouUiComponentState
import dev.mslalith.focuslauncher.feature.quoteforyou.widget.QuoteForYouUiComponentUiEvent

@CircuitInject(QuoteWidgetSettingsBottomSheetScreen::class, SingletonComponent::class)
@Composable
fun QuoteWidgetSettingsBottomSheet(
    state: QuoteWidgetSettingsBottomSheetState,
    modifier: Modifier = Modifier
) {
    // Need to extract the eventSink out to a local val, so that the Compose Compiler
    // treats it as stable. See: https://issuetracker.google.com/issues/256100927
    val eventSink = state.eventSink

    QuoteWidgetSettingsBottomSheet(
        modifier = modifier,
        state = state,
        onToggleShowQuotes = { eventSink(QuoteWidgetSettingsBottomSheetUiEvent.ToggleShowQuoteWidget) },
        onFetchQuotesClick = { eventSink(QuoteWidgetSettingsBottomSheetUiEvent.FetchQuoteWidget) },
        onQuoteClick = { eventSink(QuoteWidgetSettingsBottomSheetUiEvent.FetchNextQuote) }
    )
}

@Composable
private fun QuoteWidgetSettingsBottomSheet(
    state: QuoteWidgetSettingsBottomSheetState,
    onToggleShowQuotes: () -> Unit,
    onFetchQuotesClick: () -> Unit,
    onQuoteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        PreviewQuotes(
            state = QuoteForYouUiComponentState(
                showQuotes = state.showQuotes,
                currentQuote = state.currentQuote,
                eventSink = {
                    when (it) {
                        QuoteForYouUiComponentUiEvent.FetchNextQuote -> onQuoteClick()
                    }
                }
            )
        )
        SettingsSelectableSwitchItem(
            text = stringResource(id = R.string.enable_quotes),
            checked = state.showQuotes,
            onClick = onToggleShowQuotes
        )
        SettingsLoadableItem(
            text = stringResource(id = R.string.fetch_quotes),
            disabled = !state.showQuotes,
            isLoading = state.isFetchingQuotes,
            onClick = onFetchQuotesClick
        )
        VerticalSpacer(spacing = 12.dp)
    }
}
