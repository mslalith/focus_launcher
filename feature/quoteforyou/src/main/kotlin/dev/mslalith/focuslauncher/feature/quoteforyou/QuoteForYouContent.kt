package dev.mslalith.focuslauncher.feature.quoteforyou

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mslalith.focuslauncher.core.model.Quote
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer

@Composable
internal fun QuoteForYouContent(
    quote: Quote,
    onQuoteClick: () -> Unit,
    backgroundColor: Color,
) {
    Column(
        modifier = Modifier
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = backgroundColor)
            .clickable { onQuoteClick() }
            .padding(horizontal = 36.dp)
            .padding(
                top = 16.dp,
                bottom = 28.dp
            )
            .animateContentSize()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_format_quote),
            contentDescription = "Quotation",
            tint = MaterialTheme.colors.onBackground,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        VerticalSpacer(spacing = 12.dp)
        Crossfade(targetState = quote.quote) { quote ->
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = quote,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 14.sp,
                    letterSpacing = 1.sp
                )
            )
        }
        VerticalSpacer(spacing = 16.dp)
        Crossfade(targetState = quote.author) { author ->
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "— $author",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
            )
        }
    }
}
