package dev.mslalith.focuslauncher.feature.lunarcalendar.widget.ui

import androidx.compose.animation.Crossfade
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.mslalith.focuslauncher.core.common.extensions.asPercent
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhaseDetails
import dev.mslalith.focuslauncher.core.ui.extensions.string

@Composable
internal fun LunarPhaseName(
    modifier: Modifier = Modifier,
    lunarPhaseDetails: LunarPhaseDetails,
    showIlluminationPercent: Boolean,
    textColor: Color
) {
    val phaseNameAndIlluminationPercentPair = lunarPhaseDetails.run {
        lunarPhase.phaseNameUiText.string() to (illumination * 100).asPercent()
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
            color = textColor,
            style = MaterialTheme.typography.titleMedium
        )
    }
}
