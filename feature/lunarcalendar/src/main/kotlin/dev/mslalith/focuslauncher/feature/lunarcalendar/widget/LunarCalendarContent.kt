package dev.mslalith.focuslauncher.feature.lunarcalendar.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.common.State
import dev.mslalith.focuslauncher.core.common.getOrNull
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhaseDetails
import dev.mslalith.focuslauncher.core.model.lunarphase.UpcomingLunarPhase
import dev.mslalith.focuslauncher.feature.lunarcalendar.shared.LunarPhaseMoonIcon

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun LunarCalendarContent(
    lunarPhaseDetails: State<LunarPhaseDetails>,
    upcomingLunarPhase: State<UpcomingLunarPhase>,
    showIlluminationPercent: Boolean,
    showUpcomingPhaseDetails: Boolean,
    height: Dp = 74.dp,
    iconSize: Dp = 40.dp,
    horizontalPadding: Dp = 0.dp,
    onClick: (() -> Unit)? = null
) {
    ListItem(
        modifier = Modifier
            .height(height = height)
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(horizontal = horizontalPadding),
        icon = {
            lunarPhaseDetails.getOrNull()?.let {
                LunarPhaseMoonIcon(
                    phaseAngle = it.phaseAngle,
                    illumination = it.illumination,
                    moonSize = iconSize
                )
            }
        },
        text = {
            lunarPhaseDetails.getOrNull()?.let {
                LunarPhaseName(
                    lunarPhaseDetails = it,
                    showIlluminationPercent = showIlluminationPercent
                )
            }
        },
        secondaryText = if (showUpcomingPhaseDetails) {
            @Composable {
                upcomingLunarPhase.getOrNull()?.let {
                    UpcomingLunarPhaseDetails(
                        upcomingLunarPhase = it,
                        textColor = MaterialTheme.colors.onBackground.copy(alpha = 0.8f)
                    )
                }
            }
        } else {
            null
        }
    )
}
