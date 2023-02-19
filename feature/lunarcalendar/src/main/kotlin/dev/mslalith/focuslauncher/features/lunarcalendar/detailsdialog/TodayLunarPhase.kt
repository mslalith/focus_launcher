package dev.mslalith.focuslauncher.features.lunarcalendar.detailsdialog

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhaseDetails
import dev.mslalith.focuslauncher.core.ui.HorizontalSpacer

@Composable
internal fun TodayLunarPhase(
    lunarPhaseDetails: LunarPhaseDetails
) {
    BoxWithConstraints {
        val width = maxWidth
        Row(modifier = Modifier.fillMaxWidth()) {
            TodayLunarMoonIconAndPhase(
                lunarPhaseDetails = lunarPhaseDetails,
                moonSize = width
            )
            HorizontalSpacer(spacing = 12.dp)
            TodayLunarMoonPhaseDetails(lunarPhaseDetails = lunarPhaseDetails)
        }
    }
}
