package dev.mslalith.focuslauncher.features.quoteforyou

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import dev.mslalith.focuslauncher.core.common.getOrNull

@Composable
fun QuoteForYou(
    modifier: Modifier = Modifier,
    quoteForYouViewModel: QuoteForYouViewModel = hiltViewModel(),
    backgroundColor: Color = MaterialTheme.colors.primaryVariant,
) {
    val quoteForYouState by quoteForYouViewModel.quoteForYouState.collectAsState()

    AnimatedVisibility(
        visible = quoteForYouState.showQuotes,
        modifier = modifier
    ) {
        val quote = quoteForYouState.currentQuote.getOrNull() ?: return@AnimatedVisibility

        QuoteForYouContent(
            quote = quote,
            onQuoteClick = quoteForYouViewModel::nextRandomQuote,
            backgroundColor = backgroundColor,
        )
    }
}
