package dev.mslalith.focuslauncher.feature.lunarcalendar.widget.ui

import androidx.compose.animation.Crossfade
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import dev.mslalith.focuslauncher.core.common.extensions.inShortReadableFormat
import dev.mslalith.focuslauncher.core.model.lunarphase.UpcomingLunarPhase
import dev.mslalith.focuslauncher.core.ui.extensions.string
import dev.mslalith.focuslauncher.feature.lunarcalendar.R

@Composable
internal fun UpcomingLunarPhaseDetails(
    modifier: Modifier = Modifier,
    upcomingLunarPhase: UpcomingLunarPhase,
    textColor: Color
) {
    val phaseName = upcomingLunarPhase.lunarPhase.phaseNameUiText.string()
    val dateTime = upcomingLunarPhase.dateTime?.inShortReadableFormat() ?: return
    val nextPhaseOnText = stringResource(id = R.string.next_phase_is_on_date, phaseName, dateTime)

    Crossfade(
        modifier = modifier,
        label = "Cross Fade Upcoming Lunar Phase Details",
        targetState = nextPhaseOnText
    ) {
        Text(
            text = it,
            color = textColor
        )
    }
}
