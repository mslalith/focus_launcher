package dev.mslalith.focuslauncher.feature.quoteforyou.model

import androidx.compose.runtime.Immutable
import dev.mslalith.focuslauncher.core.common.model.State
import dev.mslalith.focuslauncher.core.model.Quote

@Immutable
internal data class QuoteForYouState(
    val showQuotes: Boolean,
    val currentQuote: State<Quote>
)
