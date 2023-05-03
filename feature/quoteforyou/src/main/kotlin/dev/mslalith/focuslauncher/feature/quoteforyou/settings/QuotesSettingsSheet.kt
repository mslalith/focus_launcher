package dev.mslalith.focuslauncher.feature.quoteforyou.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.settings.SettingsLoadableItem
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableSwitchItem
import dev.mslalith.focuslauncher.feature.quoteforyou.QuoteForYouViewModel
import dev.mslalith.focuslauncher.feature.quoteforyou.R

@Composable
fun QuotesSettingsSheet() {
    QuotesSettingsSheetInternal()
}

@Composable
internal fun QuotesSettingsSheetInternal(
    quoteForYouViewModel: QuoteForYouViewModel = hiltViewModel()
) {
    val quoteForYouState by quoteForYouViewModel.quoteForYouState.collectAsStateWithLifecycle()
    val isFetchingQuotes by quoteForYouViewModel.isFetchingQuotes.collectAsStateWithLifecycle()

    Column {
        PreviewQuotes(showQuotes = quoteForYouState.showQuotes)
        SettingsSelectableSwitchItem(
            text = stringResource(id = R.string.enable_quotes),
            checked = quoteForYouState.showQuotes,
            onClick = quoteForYouViewModel::toggleShowQuotes
        )
        SettingsLoadableItem(
            text = stringResource(id = R.string.fetch_quotes),
            disabled = !quoteForYouState.showQuotes,
            isLoading = isFetchingQuotes,
            onClick = quoteForYouViewModel::fetchQuotesIfRequired
        )
        VerticalSpacer(spacing = 12.dp)
    }
}
