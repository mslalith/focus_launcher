package dev.mslalith.focuslauncher.feature.homepage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.feature.homepage.model.LocalHomePadding
import dev.mslalith.focuslauncher.feature.lunarcalendar.widget.LunarCalendarUiComponent
import dev.mslalith.focuslauncher.feature.lunarcalendar.widget.LunarCalendarUiComponentState

@Composable
internal fun DecoratedLunarCalendar(
    state: LunarCalendarUiComponentState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val homePadding = LocalHomePadding.current
    val startPadding = homePadding.contentPaddingValues.calculateStartPadding(layoutDirection = LayoutDirection.Ltr)
    val startOffsetPadding = startPadding - 16.dp
    val extraLunarPhaseIconSize = 1.dp
    val iconSize = homePadding.lunarPhaseIconSize + extraLunarPhaseIconSize

    Box(modifier = modifier) {
        LunarCalendarUiComponent(
            state = state,
            iconSize = iconSize,
            horizontalPadding = startOffsetPadding,
            onClick = onClick
        )
    }
}
