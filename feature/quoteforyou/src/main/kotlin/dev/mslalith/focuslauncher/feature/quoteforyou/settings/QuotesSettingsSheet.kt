package dev.mslalith.focuslauncher.feature.quoteforyou.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.settings.SettingsSelectableItem
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
    val quoteForYouState by quoteForYouViewModel.quoteForYouState.collectAsState()

    val isFetchingQuotes by quoteForYouViewModel.isFetchingQuotes.collectAsState()

    fun fetchQuotesTrailingIcon(): @Composable () -> Unit {
        return if (isFetchingQuotes) {
            @Composable {
                Box(
                    modifier = Modifier
                        .fillMaxHeight(fraction = 0.5f)
                        .aspectRatio(ratio = 1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.onBackground,
                        strokeWidth = 2.dp
                    )
                }
            }
        } else {
            @Composable {}
        }
    }

    Column {
        PreviewQuotes(showQuotes = quoteForYouState.showQuotes)
        SettingsSelectableSwitchItem(
            text = "Enable Quotes",
            checked = quoteForYouState.showQuotes,
            onClick = quoteForYouViewModel::toggleShowQuotes
        )
        SettingsSelectableItem(
            text = "Fetch Quotes",
            disabled = !quoteForYouState.showQuotes,
            onClick = quoteForYouViewModel::fetchQuotesIfRequired,
            trailing = fetchQuotesTrailingIcon()
        )
        VerticalSpacer(spacing = 12.dp)
    }
}
