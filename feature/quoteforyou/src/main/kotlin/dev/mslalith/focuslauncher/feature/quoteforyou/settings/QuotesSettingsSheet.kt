package dev.mslalith.focuslauncher.feature.quoteforyou.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.settings.SettingsLoadableItem
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableSwitchItem
import dev.mslalith.focuslauncher.feature.quoteforyou.QuoteForYouViewModel

@Composable
fun QuotesSettingsSheet() {
    QuotesSettingsSheet(
        quoteForYouViewModel = hiltViewModel()
    )
}

@Composable
internal fun QuotesSettingsSheet(
    quoteForYouViewModel: QuoteForYouViewModel
) {
    val quoteForYouState by quoteForYouViewModel.quoteForYouState.collectAsStateWithLifecycle()

    val isFetchingQuotes by quoteForYouViewModel.isFetchingQuotes.collectAsStateWithLifecycle()

    Column {
        PreviewQuotes(showQuotes = quoteForYouState.showQuotes)
        SettingsSelectableSwitchItem(
            text = "Enable Quotes",
            checked = quoteForYouState.showQuotes,
            onClick = quoteForYouViewModel::toggleShowQuotes
        )
        SettingsLoadableItem(
            text = "Fetch Quotes",
            disabled = !quoteForYouState.showQuotes,
            isLoading = isFetchingQuotes,
            onClick = quoteForYouViewModel::fetchQuotesIfRequired,
        )
        VerticalSpacer(spacing = 12.dp)
    }
}
