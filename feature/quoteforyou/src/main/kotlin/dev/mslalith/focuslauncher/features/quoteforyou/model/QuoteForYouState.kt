package dev.mslalith.focuslauncher.features.quoteforyou.model

import dev.mslalith.focuslauncher.core.common.State
import dev.mslalith.focuslauncher.core.model.Quote

data class QuoteForYouState(
    val showQuotes: Boolean,
    val currentQuote: State<Quote>
)
