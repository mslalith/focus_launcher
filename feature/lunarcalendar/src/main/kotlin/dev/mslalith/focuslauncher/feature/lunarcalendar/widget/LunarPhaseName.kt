package dev.mslalith.focuslauncher.feature.lunarcalendar.widget

import androidx.compose.animation.Crossfade
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.mslalith.focuslauncher.core.common.extensions.asPercent
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhaseDetails

@Composable
internal fun LunarPhaseName(
    modifier: Modifier = Modifier,
    lunarPhaseDetails: LunarPhaseDetails,
    showIlluminationPercent: Boolean,
    textColor: Color
) {
    val phaseNameAndIlluminationPercentPair = lunarPhaseDetails.run {
        lunarPhase.phaseName to (illumination * 100).asPercent()
    }
    val text = phaseNameAndIlluminationPercentPair.let {
        it.first + if (showIlluminationPercent) " (${it.second})" else ""
    }
    Crossfade(
        modifier = modifier,
        label = "Cross Fade Lunar Phase name",
        targetState = text
    ) {
        Text(
            text = it,
            style = TextStyle(
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.2.sp
            )
        )
    }
}
