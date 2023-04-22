package dev.mslalith.focuslauncher.feature.lunarcalendar.detailsdialog

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.common.extensions.asPercent
import dev.mslalith.focuslauncher.core.common.extensions.limitDecimals
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhaseDetails
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer

@Composable
internal fun TodayLunarMoonPhaseDetails(
    lunarPhaseDetails: LunarPhaseDetails,
    contentColor: Color
) {
    Column {
        Text(
            text = lunarPhaseDetails.lunarPhase.phaseName,
            color = contentColor,
            style = MaterialTheme.typography.titleMedium
        )
        VerticalSpacer(spacing = 8.dp)
        Text(
            text = "Illumination: ${lunarPhaseDetails.illumination.times(100).asPercent(precision = 3)}",
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium
        )
        VerticalSpacer(spacing = 4.dp)
        Text(
            text = "Angle: ${lunarPhaseDetails.phaseAngle.limitDecimals(precision = 2)}",
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
