package dev.mslalith.focuslauncher.feature.lunarcalendar.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.feature.lunarcalendar.widget.ui.LunarCalendarContent

@Composable
fun LunarCalendarUiComponent(
    state: LunarCalendarUiComponentState,
    modifier: Modifier = Modifier,
    iconSize: Dp = 40.dp,
    horizontalPadding: Dp = 0.dp,
    backgroundColor: Color = Color.Transparent,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: (() -> Unit)? = null
) {
    LunarCalendarUiComponentInternal(
        modifier = modifier,
        state = state,
        iconSize = iconSize,
        horizontalPadding = horizontalPadding,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        onClick = onClick
    )
}

@Composable
private fun LunarCalendarUiComponentInternal(
    state: LunarCalendarUiComponentState,
    modifier: Modifier = Modifier,
    height: Dp = 74.dp,
    iconSize: Dp = 40.dp,
    horizontalPadding: Dp = 0.dp,
    backgroundColor: Color = Color.Transparent,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: (() -> Unit)? = null
) {
    AnimatedVisibility(
        visible = state.showLunarPhase,
        modifier = modifier,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LunarCalendarContent(
            lunarPhaseDetails = state.lunarPhaseDetails,
            upcomingLunarPhase = state.upcomingLunarPhase,
            showIlluminationPercent = state.showIlluminationPercent,
            showUpcomingPhaseDetails = state.showUpcomingPhaseDetails,
            height = height,
            iconSize = iconSize,
            horizontalPadding = horizontalPadding,
            backgroundColor = backgroundColor,
            contentColor = contentColor,
            onClick = onClick
        )
    }
}
