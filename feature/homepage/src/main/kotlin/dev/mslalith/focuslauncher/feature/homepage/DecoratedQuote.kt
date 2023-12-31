package dev.mslalith.focuslauncher.feature.homepage

import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.feature.homepage.model.LocalHomePadding
import dev.mslalith.focuslauncher.feature.quoteforyou.widget.QuoteForYouUiComponent
import dev.mslalith.focuslauncher.feature.quoteforyou.widget.QuoteForYouUiComponentState

@Composable
internal fun DecoratedQuote(
    state: QuoteForYouUiComponentState,
    modifier: Modifier = Modifier
) {
    val homePadding = LocalHomePadding.current
    val startPadding = homePadding.contentPaddingValues.calculateStartPadding(layoutDirection = LayoutDirection.Ltr)

    QuoteForYouUiComponent(
        state = state,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = startPadding,
                vertical = 8.dp
            )
    )
}
