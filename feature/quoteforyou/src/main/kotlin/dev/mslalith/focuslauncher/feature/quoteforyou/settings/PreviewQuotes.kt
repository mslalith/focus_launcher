package dev.mslalith.focuslauncher.feature.quoteforyou.settings

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.feature.quoteforyou.QuoteForYou

@Composable
internal fun PreviewQuotes(
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
