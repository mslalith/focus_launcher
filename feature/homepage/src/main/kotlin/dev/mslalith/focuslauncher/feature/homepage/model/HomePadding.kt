package dev.mslalith.focuslauncher.feature.homepage.model

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal val LocalHomePadding = staticCompositionLocalOf<HomePadding> {
    error("No LocalHomePadding provided")
}

internal data class HomePadding(
    val contentPaddingValues: PaddingValues = PaddingValues(
        start = 22.dp,
        end = 22.dp,
        top = 16.dp,
        bottom = 22.dp
    ),
    val lunarPhaseIconSize: Dp = 40.dp,
    val favoriteActionItemSize: Dp = 16.dp
)
