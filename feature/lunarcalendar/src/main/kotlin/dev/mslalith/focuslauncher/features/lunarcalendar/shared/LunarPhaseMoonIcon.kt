package dev.mslalith.focuslauncher.features.lunarcalendar.shared

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun LunarPhaseMoonIcon(
    modifier: Modifier = Modifier,
    phaseAngle: Double,
    illumination: Double,
    moonSize: Dp = 40.dp
) {
    val illuminatedColor = Color(0xFFBCC1C5)
    val moonColor = Color(0xFF66757F)
    val moonSpotColor = Color(0xFF5B6876)

    val rotationDegrees by animateFloatAsState(
        targetValue = when (phaseAngle < 0) {
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
            fraction = illumination.toFloat()
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
                        y = radius * .5f
                    )
                )

                // diagonal center 2
                (radius * .15f).let {
                    drawCircle(
                        color = moonSpotColor,
                        radius = it,
                        center = Offset(
                            x = radius + (it / 2f),
                            y = radius - (it / 2f)
                        )
                    )
                }

                // diagonal center 3
                drawCircle(
                    color = moonSpotColor,
                    radius = radius * .2f,
                    center = Offset(
                        x = radius * 1.35f,
                        y = radius * 1.55f
                    )
                )

                // diagonal top 1
                drawCircle(
                    color = moonSpotColor,
                    radius = radius * .1f,
                    center = Offset(
                        x = radius * 1.2f,
                        y = radius * .3f
                    )
                )

                // diagonal top 2
                drawCircle(
                    color = moonSpotColor,
                    radius = radius * .06f,
                    center = Offset(
                        x = radius * 1.65f,
                        y = radius * .55f
                    )
                )

                // diagonal top 3
                drawCircle(
                    color = moonSpotColor,
                    radius = radius * .1f,
                    center = Offset(
                        x = radius * 1.7f,
                        y = radius
                    )
                )

                // diagonal bottom 1
                drawCircle(
                    color = moonSpotColor,
                    radius = radius * .06f,
                    center = Offset(
                        x = radius * .2f,
                        y = radius
                    )
                )

                // diagonal bottom 2
                drawCircle(
                    color = moonSpotColor,
                    radius = radius * .1f,
                    center = Offset(
                        x = radius * .65f,
                        y = radius * 1.3f
                    )
                )

                // diagonal bottom 3
                drawCircle(
                    color = moonSpotColor,
                    radius = radius * .06f,
                    center = Offset(
                        x = radius * .8f,
                        y = radius * 1.75f
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
                            y3 = 0f
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
