package dev.mslalith.focuslauncher.feature.quoteforyou.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.common.model.getOrNull
import dev.mslalith.focuslauncher.core.model.Quote
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.feature.quoteforyou.R

@Composable
fun QuoteForYouUiComponent(
    state: QuoteForYouUiComponentState,
    modifier: Modifier = Modifier
) {
    // Need to extract the eventSink out to a local val, so that the Compose Compiler
    // treats it as stable. See: https://issuetracker.google.com/issues/256100927
    val eventSink = state.eventSink

    QuoteForYouUiComponent(
        modifier = modifier,
        state = state,
        onQuoteClick = { eventSink(QuoteForYouUiComponentUiEvent.FetchNextQuote) }
    )
}

@Composable
private fun QuoteForYouUiComponent(
    state: QuoteForYouUiComponentState,
    modifier: Modifier = Modifier,
    onQuoteClick: () -> Unit,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer
) {
    AnimatedVisibility(
        visible = state.showQuotes,
        modifier = modifier
    ) {
        val quote = state.currentQuote.getOrNull() ?: return@AnimatedVisibility

        QuoteForYouContent(
            quote = quote,
            onQuoteClick = onQuoteClick,
            backgroundColor = backgroundColor,
            contentColor = contentColor
        )
    }
}

@Composable
private fun QuoteForYouContent(
    quote: Quote,
    onQuoteClick: () -> Unit,
    backgroundColor: Color,
    contentColor: Color
) {
    Column(
        modifier = Modifier
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = backgroundColor)
            .clickable { onQuoteClick() }
            .padding(horizontal = 36.dp)
            .padding(
                top = 16.dp,
                bottom = 28.dp
            )
            .animateContentSize()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_quote),
            contentDescription = stringResource(id = R.string.quotation_icon),
            tint = contentColor,
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        )
        VerticalSpacer(spacing = 12.dp)
        Crossfade(
            label = "Cross Fade Quote",
            targetState = quote.quote
        ) { quote ->
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = quote,
                textAlign = TextAlign.Center,
                color = contentColor,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        VerticalSpacer(spacing = 16.dp)
        Crossfade(
            label = "Cross Fade Quote Author",
            targetState = quote.author
        ) { author ->
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "— $author",
                textAlign = TextAlign.Center,
                color = contentColor,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
