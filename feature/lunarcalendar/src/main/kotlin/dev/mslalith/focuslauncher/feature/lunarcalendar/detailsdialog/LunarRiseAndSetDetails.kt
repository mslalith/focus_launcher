package dev.mslalith.focuslauncher.feature.lunarcalendar.detailsdialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhaseDetails
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer

@Composable
internal fun LunarRiseAndSetDetails(
    lunarPhaseDetails: LunarPhaseDetails
) {
    RiseAndSetHeaders()
    VerticalSpacer(spacing = 16.dp)
    RiseTimeDetails(
        moonRiseDateTime = lunarPhaseDetails.moonRiseAndSetDetails.riseDateTime,
        sunRiseDateTime = lunarPhaseDetails.sunRiseAndSetDetails.riseDateTime
    )
    VerticalSpacer(spacing = 4.dp)
    SetTimeDetails(
        moonSetDateTime = lunarPhaseDetails.moonRiseAndSetDetails.setDateTime,
        sunSetDateTime = lunarPhaseDetails.sunRiseAndSetDetails.setDateTime
    )
}
