package dev.mslalith.focuslauncher.feature.quoteforyou.settings

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.feature.quoteforyou.R
import dev.mslalith.focuslauncher.feature.quoteforyou.widget.QuoteForYouUiComponent
import dev.mslalith.focuslauncher.feature.quoteforyou.widget.QuoteForYouUiComponentState

@Composable
internal fun PreviewQuotes(
    state: QuoteForYouUiComponentState,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer
) {
    val height = 72.dp
    val horizontalPadding = 24.dp

    Crossfade(
        label = "Cross Fade Preview Quotes",
        targetState = state.showQuotes,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding)
            .padding(top = 16.dp, bottom = 16.dp)
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = backgroundColor)
    ) {
        if (it) {
            QuoteForYouUiComponent(
                state = state,
                backgroundColor = backgroundColor,
                contentColor = contentColor
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = height),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.enable_quotes_to_preview),
                    textAlign = TextAlign.Center,
                    color = contentColor
                )
            }
        }
    }
}
