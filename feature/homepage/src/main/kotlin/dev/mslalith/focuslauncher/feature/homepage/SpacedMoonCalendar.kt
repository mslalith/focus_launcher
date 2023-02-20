package dev.mslalith.focuslauncher.feature.homepage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.feature.homepage.model.LocalHomePadding
import dev.mslalith.focuslauncher.feature.lunarcalendar.widget.LunarCalendar

@Composable
internal fun SpacedMoonCalendar(
    modifier: Modifier = Modifier,
    onMoonCalendarClick: () -> Unit
) {
    val homePadding = LocalHomePadding.current
    val startPadding = homePadding.contentPaddingValues.calculateStartPadding(LayoutDirection.Ltr)
    val startOffsetPadding = startPadding - 16.dp
    val extraLunarPhaseIconSize = 1.dp
    val iconSize = homePadding.lunarPhaseIconSize + extraLunarPhaseIconSize

    Box(modifier = modifier) {
        LunarCalendar(
            iconSize = iconSize,
            horizontalPadding = startOffsetPadding,
            onClick = onMoonCalendarClick
        )
    }
}
