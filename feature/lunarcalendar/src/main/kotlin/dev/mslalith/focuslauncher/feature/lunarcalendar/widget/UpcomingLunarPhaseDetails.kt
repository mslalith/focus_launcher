package dev.mslalith.focuslauncher.feature.lunarcalendar.widget

import androidx.compose.animation.Crossfade
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import dev.mslalith.focuslauncher.core.common.extensions.inShortReadableFormat
import dev.mslalith.focuslauncher.core.model.lunarphase.UpcomingLunarPhase

@Composable
internal fun UpcomingLunarPhaseDetails(
    modifier: Modifier = Modifier,
    upcomingLunarPhase: UpcomingLunarPhase,
    textColor: Color
) {
    val phaseName = upcomingLunarPhase.lunarPhase.phaseName
    val dateTime = upcomingLunarPhase.dateTime?.inShortReadableFormat() ?: return
    val nextPhaseOnText = "next $phaseName is on $dateTime"

    Crossfade(
        modifier = modifier,
        label = "Cross Fade Upcoming Lunar Phase Details",
        targetState = nextPhaseOnText
    ) {
        Text(
            text = it,
            style = TextStyle(
                color = textColor,
                fontSize = 12.sp,
                letterSpacing = 0.9.sp
            )
        )
    }
}
