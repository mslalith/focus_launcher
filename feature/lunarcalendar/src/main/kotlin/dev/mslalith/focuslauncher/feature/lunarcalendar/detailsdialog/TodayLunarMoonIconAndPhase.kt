package dev.mslalith.focuslauncher.feature.lunarcalendar.detailsdialog

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhaseDetails
import dev.mslalith.focuslauncher.feature.lunarcalendar.shared.LunarPhaseMoonIcon

@Composable
internal fun TodayLunarMoonIconAndPhase(
    lunarPhaseDetails: LunarPhaseDetails,
    moonSize: Dp
) {
    Column {
        LunarPhaseMoonIcon(
            phaseAngle = lunarPhaseDetails.phaseAngle,
            illumination = lunarPhaseDetails.illumination,
            moonSize = moonSize * 0.3f
        )
    }
}
