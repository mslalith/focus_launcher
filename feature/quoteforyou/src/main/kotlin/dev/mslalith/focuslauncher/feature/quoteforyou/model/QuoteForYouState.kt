package dev.mslalith.focuslauncher.feature.quoteforyou.model

import dev.mslalith.focuslauncher.core.common.State
import dev.mslalith.focuslauncher.core.model.Quote

internal data class QuoteForYouState(
    val showQuotes: Boolean,
    val currentQuote: State<Quote>
)
