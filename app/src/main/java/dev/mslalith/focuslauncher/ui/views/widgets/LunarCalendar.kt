package dev.mslalith.focuslauncher.ui.views.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mslalith.focuslauncher.data.models.LunarPhaseDetails
import dev.mslalith.focuslauncher.data.models.Outcome
import dev.mslalith.focuslauncher.data.models.UpcomingLunarPhase
import dev.mslalith.focuslauncher.extensions.asPercent
import dev.mslalith.focuslauncher.extensions.inShortReadableFormat
import dev.mslalith.focuslauncher.ui.viewmodels.SettingsViewModel
import dev.mslalith.focuslauncher.ui.viewmodels.WidgetsViewModel

@Composable
fun LunarCalendar(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel,
    widgetsViewModel: WidgetsViewModel,
    height: Dp = 74.dp,
    iconSize: Dp = 40.dp,
    horizontalPadding: Dp = 0.dp,
) {
    val showLunarPhase by settingsViewModel.showLunarPhaseStateFlow.collectAsState()
    val showIlluminationPercent by settingsViewModel.showIlluminationPercentStateFlow.collectAsState()
    val showUpcomingPhaseDetails by settingsViewModel.showUpcomingPhaseDetailsStateFlow.collectAsState()

    val lunarPhaseDetails by widgetsViewModel.lunarPhaseDetailsStateFlow.collectAsState()
    val upcomingLunarPhase by widgetsViewModel.upcomingLunarPhaseStateFlow.collectAsState()

    AnimatedVisibility(
        visible = showLunarPhase,
        modifier = modifier,
    ) {
        LunarCalendarContent(
            lunarPhaseDetails = lunarPhaseDetails,
            upcomingLunarPhase = upcomingLunarPhase,
            showIlluminationPercent = showIlluminationPercent,
            showUpcomingPhaseDetails = showUpcomingPhaseDetails,
            height = height,
            iconSize = iconSize,
            horizontalPadding = horizontalPadding,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LunarCalendarContent(
    lunarPhaseDetails: Outcome<LunarPhaseDetails>,
    upcomingLunarPhase: Outcome<UpcomingLunarPhase>,
    showIlluminationPercent: Boolean,
    showUpcomingPhaseDetails: Boolean,
    height: Dp = 74.dp,
    iconSize: Dp = 40.dp,
    horizontalPadding: Dp = 0.dp,
) {
    ListItem(
        modifier = Modifier
            .height(height = height)
            .padding(horizontal = horizontalPadding),
        icon = {
            (lunarPhaseDetails as? Outcome.Success)?.value?.let {
                LunarPhaseMoonIcon(
                    lunarPhaseDetails = it,
                    moonSize = iconSize,
                )
            }
        },
        text = {
            (lunarPhaseDetails as? Outcome.Success)?.value?.let {
                LunarPhaseName(
                    lunarPhaseDetails = it,
                    showIlluminationPercent = showIlluminationPercent,
                )
            }
        },
        secondaryText = if (showUpcomingPhaseDetails) {
            {
                (upcomingLunarPhase as? Outcome.Success)?.value?.let {
                    UpcomingLunarPhaseDetails(
                        upcomingLunarPhase = it,
                        textColor = MaterialTheme.colors.onBackground.copy(alpha = 0.8f)
                    )
                }
            }
        } else null,
    )
}

@Composable
private fun LunarPhaseMoonIcon(
    modifier: Modifier = Modifier,
    lunarPhaseDetails: LunarPhaseDetails,
    moonSize: Dp = 40.dp,
) {
    val illuminatedColor = Color(0xFFBCC1C5)
    val moonColor = Color(0xFF66757F)
    val moonSpotColor = Color(0xFF5B6876)

    val rotationDegrees by animateFloatAsState(
        targetValue = when (lunarPhaseDetails.phaseAngle < 0) {
            true -> 180f
            false -> 0f
        }
    )

    val startOffset = Offset(-0.165f, 0f)
    val endOffset = Offset(1.165f, 0f)
    val percentOffset by animateOffsetAsState(
        targetValue = lerp(
            start = startOffset,
            stop = endOffset,
            fraction = lunarPhaseDetails.illumination.toFloat(),
        )
    )

    Box(
        modifier = modifier
            .size(size = moonSize)
            .clip(CircleShape)
            .drawWithContent {
                val radius = size.minDimension / 2f
                drawRect(color = moonColor)

                // diagonal center 1
                drawCircle(
                    color = moonSpotColor,
                    radius = radius * .2f,
                    center = Offset(
                        x = radius * .5f,
                        y = radius * .5f,
                    ),
                )

                // diagonal center 2
                (radius * .15f).let {
                    drawCircle(
                        color = moonSpotColor,
                        radius = it,
                        center = Offset(
                            x = radius + (it / 2f),
                            y = radius - (it / 2f),
                        )
                    )
                }

                // diagonal center 3
                drawCircle(
                    color = moonSpotColor,
                    radius = radius * .2f,
                    center = Offset(
                        x = radius * 1.35f,
                        y = radius * 1.55f,
                    )
                )

                // diagonal top 1
                drawCircle(
                    color = moonSpotColor,
                    radius = radius * .1f,
                    center = Offset(
                        x = radius * 1.2f,
                        y = radius * .3f,
                    )
                )

                // diagonal top 2
                drawCircle(
                    color = moonSpotColor,
                    radius = radius * .06f,
                    center = Offset(
                        x = radius * 1.65f,
                        y = radius * .55f,
                    )
                )

                // diagonal top 3
                drawCircle(
                    color = moonSpotColor,
                    radius = radius * .1f,
                    center = Offset(
                        x = radius * 1.7f,
                        y = radius,
                    )
                )

                // diagonal bottom 1
                drawCircle(
                    color = moonSpotColor,
                    radius = radius * .06f,
                    center = Offset(
                        x = radius * .2f,
                        y = radius,
                    )
                )

                // diagonal bottom 2
                drawCircle(
                    color = moonSpotColor,
                    radius = radius * .1f,
                    center = Offset(
                        x = radius * .65f,
                        y = radius * 1.3f,
                    )
                )

                // diagonal bottom 3
                drawCircle(
                    color = moonSpotColor,
                    radius = radius * .06f,
                    center = Offset(
                        x = radius * .8f,
                        y = radius * 1.75f,
                    )
                )


                Path()
                    .apply {
                        moveTo(radius, 0f)
                        quadraticBezierTo(
                            x1 = 0f,
                            y1 = 0f,
                            x2 = 0f,
                            y2 = radius
                        )
                        quadraticBezierTo(
                            x1 = 0f,
                            y1 = size.height,
                            x2 = radius,
                            y2 = size.height
                        )
                        cubicTo(
                            x1 = size.width * percentOffset.x,
                            y1 = size.height,
                            x2 = size.width * percentOffset.x,
                            y2 = 0f,
                            x3 = size.width * .5f,
                            y3 = 0f,
                        )
                        close()
                    }
                    .also {
                        rotate(degrees = rotationDegrees) {
                            drawPath(
                                path = it,
                                color = illuminatedColor,
                                blendMode = BlendMode.Overlay
                            )
                        }
                    }
            }
    )
}

@Composable
private fun LunarPhaseName(
    modifier: Modifier = Modifier,
    lunarPhaseDetails: LunarPhaseDetails,
    showIlluminationPercent: Boolean,
    textColor: Color = MaterialTheme.colors.onBackground,
) {
    val phaseNameAndIlluminationPercentPair = lunarPhaseDetails.run {
        lunarPhase.phaseName to (illumination * 100).asPercent()
    }
    val text = phaseNameAndIlluminationPercentPair.let {
        it.first + if (showIlluminationPercent) " (${it.second})" else ""
    }
    Crossfade(
        modifier = modifier,
        targetState = text,
    ) {
        Text(
            text = it,
            style = TextStyle(
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.2.sp,
            ),
        )
    }
}

@Composable
private fun UpcomingLunarPhaseDetails(
    modifier: Modifier = Modifier,
    upcomingLunarPhase: UpcomingLunarPhase,
    textColor: Color = MaterialTheme.colors.onBackground,
) {
    val phaseName = upcomingLunarPhase.lunarPhase.phaseName
    val dateTime = upcomingLunarPhase.dateTime?.inShortReadableFormat() ?: return
    val nextPhaseOnText = "next $phaseName is on $dateTime"

    Crossfade(
        modifier = modifier,
        targetState = nextPhaseOnText,
    ) {
        Text(
            text = it,
            style = TextStyle(
                color = textColor,
                fontSize = 12.sp,
                letterSpacing = 0.9.sp,
            ),
        )
    }
}
