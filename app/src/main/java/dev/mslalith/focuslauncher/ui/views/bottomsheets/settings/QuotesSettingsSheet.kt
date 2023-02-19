package dev.mslalith.focuslauncher.ui.views.bottomsheets.settings

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.data.models.QuotesSettingsProperties
import dev.mslalith.focuslauncher.extensions.isOnline
import dev.mslalith.focuslauncher.features.quoteforyou.QuoteForYou
import dev.mslalith.focuslauncher.ui.views.SettingsSelectableItem
import dev.mslalith.focuslauncher.ui.views.SettingsSelectableSwitchItem

@Composable
fun QuotesSettingsSheet(
    properties: QuotesSettingsProperties
) {
    val context = LocalContext.current

    properties.apply {
        fun fetchQuotesIfRequired() {
            if (context.isOnline) widgetsViewModel.fetchQuotesIfRequired()
        }

        val showQuotes by settingsViewModel.showQuotesStateFlow.collectAsState()
        val isFetchingQuotes by widgetsViewModel.isFetchingQuotes.collectAsState()

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
            PreviewQuotes(showQuotes = showQuotes)
            SettingsSelectableSwitchItem(
                text = "Enable Quotes",
                checked = showQuotes,
                onClick = { settingsViewModel.toggleShowQuotes() }
            )
            SettingsSelectableItem(
                text = "Fetch Quotes",
                disabled = !showQuotes,
                onClick = { fetchQuotesIfRequired() },
                trailing = fetchQuotesTrailingIcon()
            )
            VerticalSpacer(spacing = bottomSpacing)
        }
    }
}

@Composable
private fun PreviewQuotes(
    showQuotes: Boolean
) {
    val height = 72.dp
    val horizontalPadding = 24.dp

    Crossfade(
        targetState = showQuotes,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding)
            .padding(top = 16.dp, bottom = 16.dp)
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = MaterialTheme.colors.secondaryVariant)
    ) {
        if (it) {
            QuoteForYou(
                backgroundColor = MaterialTheme.colors.secondaryVariant
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = height),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Enable Quotes to preview",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        color = MaterialTheme.colors.onBackground
                    )
                )
            }
        }
    }
}
