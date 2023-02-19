package dev.mslalith.focuslauncher.features.lunarcalendar.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.mslalith.focuslauncher.features.lunarcalendar.LunarCalendarViewModel
import dev.mslalith.focuslauncher.features.lunarcalendar.model.LunarCalendarState

@Composable
fun LunarCalendar(
    modifier: Modifier = Modifier,
    lunarCalendarViewModel: LunarCalendarViewModel = hiltViewModel(),
    height: Dp = 74.dp,
    iconSize: Dp = 40.dp,
    horizontalPadding: Dp = 0.dp,
    onClick: (() -> Unit)? = null,
) {
    val lunarCalendarState by lunarCalendarViewModel.lunarCalendarState.collectAsState()

    LunarCalendar(
        modifier = modifier,
        lunarCalendarState = lunarCalendarState,
        height = height,
        iconSize = iconSize,
        horizontalPadding = horizontalPadding,
        onClick = onClick
    )
}

@Composable
private fun LunarCalendar(
    modifier: Modifier = Modifier,
    lunarCalendarState: LunarCalendarState,
    height: Dp = 74.dp,
    iconSize: Dp = 40.dp,
    horizontalPadding: Dp = 0.dp,
    onClick: (() -> Unit)? = null,
) {
    AnimatedVisibility(
        visible = lunarCalendarState.showLunarPhase,
        modifier = modifier
    ) {
        LunarCalendarContent(
            lunarPhaseDetails = lunarCalendarState.lunarPhaseDetails,
            upcomingLunarPhase = lunarCalendarState.upcomingLunarPhase,
            showIlluminationPercent = lunarCalendarState.showIlluminationPercent,
            showUpcomingPhaseDetails = lunarCalendarState.showUpcomingPhaseDetails,
            height = height,
            iconSize = iconSize,
            horizontalPadding = horizontalPadding,
            onClick = onClick
        )
    }
}
