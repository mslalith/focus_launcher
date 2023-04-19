package dev.mslalith.focuslauncher.feature.lunarcalendar.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.feature.lunarcalendar.LunarCalendarViewModel
import dev.mslalith.focuslauncher.feature.lunarcalendar.model.LunarCalendarState

@Composable
fun LunarCalendar(
    modifier: Modifier = Modifier,
    height: Dp = 74.dp,
    iconSize: Dp = 40.dp,
    horizontalPadding: Dp = 0.dp,
    backgroundColor: Color = Color.Transparent,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: (() -> Unit)? = null,
) {
    LunarCalendar(
        modifier = modifier,
        lunarCalendarViewModel = hiltViewModel(),
        height = height,
        iconSize = iconSize,
        horizontalPadding = horizontalPadding,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        onClick = onClick
    )
}

@Composable
internal fun LunarCalendar(
    modifier: Modifier = Modifier,
    lunarCalendarViewModel: LunarCalendarViewModel,
    height: Dp,
    iconSize: Dp,
    horizontalPadding: Dp,
    backgroundColor: Color = Color.Transparent,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: (() -> Unit)? = null,
) {
    val lunarCalendarState by lunarCalendarViewModel.lunarCalendarState.collectAsStateWithLifecycle()

    LunarCalendar(
        modifier = modifier,
        lunarCalendarState = lunarCalendarState,
        height = height,
        iconSize = iconSize,
        horizontalPadding = horizontalPadding,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
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
    backgroundColor: Color = Color.Transparent,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: (() -> Unit)? = null,
) {
    AnimatedVisibility(
        visible = lunarCalendarState.showLunarPhase,
        modifier = modifier,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LunarCalendarContent(
            lunarPhaseDetails = lunarCalendarState.lunarPhaseDetails,
            upcomingLunarPhase = lunarCalendarState.upcomingLunarPhase,
            showIlluminationPercent = lunarCalendarState.showIlluminationPercent,
            showUpcomingPhaseDetails = lunarCalendarState.showUpcomingPhaseDetails,
            height = height,
            iconSize = iconSize,
            horizontalPadding = horizontalPadding,
            backgroundColor = backgroundColor,
            contentColor = contentColor,
            onClick = onClick
        )
    }
}
