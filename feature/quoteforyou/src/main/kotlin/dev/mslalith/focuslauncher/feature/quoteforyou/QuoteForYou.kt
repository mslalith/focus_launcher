package dev.mslalith.focuslauncher.feature.quoteforyou

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.common.getOrNull

@Composable
fun QuoteForYou(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer
) {
    QuoteForYou(
        modifier = modifier,
        quoteForYouViewModel = hiltViewModel(),
        backgroundColor = backgroundColor,
        contentColor = contentColor
    )
}

@Composable
internal fun QuoteForYou(
    modifier: Modifier = Modifier,
    quoteForYouViewModel: QuoteForYouViewModel,
    backgroundColor: Color,
    contentColor: Color
) {
    val quoteForYouState by quoteForYouViewModel.quoteForYouState.collectAsStateWithLifecycle()

    AnimatedVisibility(
        visible = quoteForYouState.showQuotes,
        modifier = modifier
    ) {
        val quote = quoteForYouState.currentQuote.getOrNull() ?: return@AnimatedVisibility

        QuoteForYouContent(
            quote = quote,
            onQuoteClick = quoteForYouViewModel::nextRandomQuote,
            backgroundColor = backgroundColor,
            contentColor = contentColor
        )
    }
}
