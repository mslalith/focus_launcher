package dev.mslalith.focuslauncher.ui.views.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mslalith.focuslauncher.R
import dev.mslalith.focuslauncher.data.model.Quote
import dev.mslalith.focuslauncher.data.model.Outcome
import dev.mslalith.focuslauncher.extensions.VerticalSpacer
import dev.mslalith.focuslauncher.extensions.modifyIf
import dev.mslalith.focuslauncher.ui.viewmodels.SettingsViewModel
import dev.mslalith.focuslauncher.ui.viewmodels.WidgetsViewModel

@Composable
fun QuoteForYou(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel,
    widgetsViewModel: WidgetsViewModel,
    backgroundColor: Color = MaterialTheme.colors.primaryVariant,
) {
    val showQuotes by settingsViewModel.showQuotesStateFlow.collectAsState()
    val currentQuoteOutcome by widgetsViewModel.currentQuoteOutcomeStateFlow.collectAsState()

    AnimatedVisibility(
        visible = showQuotes,
        modifier = modifier,
    ) {
        val quote = (currentQuoteOutcome as? Outcome.Success)?.value ?: return@AnimatedVisibility

        QuoteForYouContent(
            widgetsViewModel = widgetsViewModel,
            quote = quote,
            backgroundColor = backgroundColor,
            preview = false,
        )
    }
}

@Composable
fun QuoteForYouContent(
    widgetsViewModel: WidgetsViewModel,
    quote: Quote,
    backgroundColor: Color,
    preview: Boolean,
) {
    Column(
        modifier = Modifier
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = backgroundColor)
            .modifyIf(predicate = { !preview }) {
                clickable { widgetsViewModel.nextRandomQuote() }
            }
            .padding(horizontal = 36.dp)
            .padding(
                top = 16.dp,
                bottom = 28.dp,
            )
            .animateContentSize()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_format_quote),
            contentDescription = "Quotation",
            tint = MaterialTheme.colors.onBackground,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        VerticalSpacer(spacing = 12.dp)
        Crossfade(targetState = quote.quote) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = it,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 14.sp,
                    letterSpacing = 1.sp,
                )
            )
        }
        VerticalSpacer(spacing = 16.dp)
        Crossfade(targetState = quote.author) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "— $it",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp,
                )
            )
        }
    }
}
