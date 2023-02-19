package dev.mslalith.focuslauncher.features.lunarcalendar.detailsdialog

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mslalith.focuslauncher.core.common.extensions.asPercent
import dev.mslalith.focuslauncher.core.common.extensions.limitDecimals
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhaseDetails
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer

@Composable
internal fun TodayLunarMoonPhaseDetails(
    lunarPhaseDetails: LunarPhaseDetails,
    textColor: Color = MaterialTheme.colors.onBackground
) {
    Column {
        Text(
            text = lunarPhaseDetails.lunarPhase.phaseName,
            style = TextStyle(
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.2.sp
            )
        )
        VerticalSpacer(spacing = 8.dp)
        Text(
            text = "Illumination: ${lunarPhaseDetails.illumination.times(100).asPercent(precision = 3)}",
            style = TextStyle(
                color = textColor,
                fontSize = 14.sp,
                letterSpacing = 1.2.sp
            )
        )
        VerticalSpacer(spacing = 4.dp)
        Text(
            text = "Angle: ${lunarPhaseDetails.phaseAngle.limitDecimals(precision = 2)}",
            style = TextStyle(
                color = textColor,
                fontSize = 14.sp,
                letterSpacing = 1.2.sp
            )
        )
    }
}
