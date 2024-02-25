package dev.mslalith.focuslauncher.feature.lunarcalendar.bottomsheet.lunarphasedetails.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.common.extensions.asPercent
import dev.mslalith.focuslauncher.core.common.extensions.limitDecimals
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhaseDetails
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.extensions.string
import dev.mslalith.focuslauncher.feature.lunarcalendar.R

@Composable
internal fun TodayLunarMoonPhaseDetails(
    lunarPhaseDetails: LunarPhaseDetails,
    contentColor: Color
) {
    val illuminationText = stringResource(id = R.string.illumination_value, lunarPhaseDetails.illumination.times(other = 100).asPercent(maxFractions = 3))
    val angleText = stringResource(id = R.string.angle_value, lunarPhaseDetails.phaseAngle.limitDecimals(maxFractions = 2))

    Column {
        Text(
            text = lunarPhaseDetails.lunarPhase.phaseNameUiText.string(),
            color = contentColor,
            style = MaterialTheme.typography.titleMedium
        )
        VerticalSpacer(spacing = 8.dp)
        Text(
            text = illuminationText,
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium
        )
        VerticalSpacer(spacing = 4.dp)
        Text(
            text = angleText,
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
