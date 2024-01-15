package dev.mslalith.focuslauncher.core.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.lint.kover.IgnoreInKoverReport
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan
import kotlin.random.Random

@IgnoreInKoverReport
private val offsetToVector = TwoWayConverter<Offset, AnimationVector2D>(
    convertToVector = { AnimationVector2D(v1 = it.x, v2 = it.y) },
    convertFromVector = { Offset(x = it.v1, y = it.v2) }
)

@Composable
fun Blob(
    modifier: Modifier = Modifier,
    size: Dp,
    numberOfPoints: Int = 6,
    travelFraction: Double = 0.1,
    paddingValues: PaddingValues = PaddingValues(all = 2.dp),
    color: Color = Color.Transparent,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val sizePixels = remember(key1 = density) {
        density.run { size.toPx() }
    }

    val animatables = remember {
        List(size = numberOfPoints) {
            Animatable(
                initialValue = Offset.Zero,
                typeConverter = offsetToVector
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        while (true) {
            coroutineScope {
                animatables.forEach { animatable ->
                    launch {
                        val targetValue = Offset(
                            x = sizePixels * Random.nextDouble(from = 0.0, until = travelFraction).toFloat(),
                            y = sizePixels * Random.nextDouble(from = 0.0, until = travelFraction).toFloat()
                        )
                        animatable.animateTo(
                            targetValue = targetValue,
                            animationSpec = tween(
                                durationMillis = Random.nextInt(from = 1200, until = 1500),
                                easing = LinearEasing
                            )
                        )
                    }
                }
            }
        }
    }

    Box(
        modifier = modifier
            .padding(paddingValues = paddingValues)
            .size(size = size)
            .drawWithContent {
                val path = getPath(
                    numberOfPoints = numberOfPoints,
                    animatables = animatables
                )

                drawPath(
                    path = path,
                    color = color
                )

                clipPath(
                    path = path,
                    block = { this@drawWithContent.drawContent() }
                )
            },
        content = { content() },
        contentAlignment = Alignment.Center
    )
}

@IgnoreInKoverReport
private fun ContentDrawScope.getPath(
    numberOfPoints: Int,
    animatables: List<Animatable<Offset, AnimationVector2D>>
): Path {
    val radius = this.size.width / 2
    val anchorDistance = (4 / 3) * tan(x = Math.PI / (2 * numberOfPoints)).toFloat() * radius

    val animatedPoints = buildList {
        repeat(times = animatables.size) { i ->
            val theta = (2 * Math.PI / numberOfPoints).toFloat() * i
            val xOriginBased = radius * cos(theta)
            val yOriginBased = radius * sin(theta)
            val offset = Offset(x = radius + xOriginBased, y = radius + yOriginBased)
            add(
                if (offset.x < radius || offset.y < radius) offset - animatables[i].value
                else offset + animatables[i].value
            )
        }
    }

    val animatedPointsWithAnchors = animatedPoints.map {
        val m = -(it.x - center.x) / (it.y - center.y)
        val theta = atan(m)
        val xOff = anchorDistance * cos(theta)
        val yOff = anchorDistance * sin(theta)
        val x1 = it.x - xOff
        val y1 = it.y - yOff
        val x2 = it.x + xOff
        val y2 = it.y + yOff
        val one = Offset(x = x1, y = y1)
        val two = Offset(x = x2, y = y2)
        if (it.y >= radius) two to one else one to two
    }

    return Path().apply {
        moveTo(animatedPoints.first().x, animatedPoints.first().y)

        // connect each point with the next one
        for (i in 0 until animatedPoints.size - 1) {
            cubicTo(
                x1 = animatedPointsWithAnchors[i].second.x, y1 = animatedPointsWithAnchors[i].second.y,
                x2 = animatedPointsWithAnchors[i + 1].first.x, y2 = animatedPointsWithAnchors[i + 1].first.y,
                x3 = animatedPoints[i + 1].x, y3 = animatedPoints[i + 1].y
            )
        }

        // connect last point to first
        cubicTo(
            x1 = animatedPointsWithAnchors.last().second.x, y1 = animatedPointsWithAnchors.last().second.y,
            x2 = animatedPointsWithAnchors.first().first.x, y2 = animatedPointsWithAnchors.first().first.y,
            x3 = animatedPoints.first().x, y3 = animatedPoints.first().y
        )
    }
}

@Preview
@Composable
private fun PreviewBlob() {
    Surface {
        Blob(
            size = 120.dp,
            color = Color.Red,
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_device_mobile),
                    contentDescription = "",
                    modifier = Modifier.size(size = 60.dp)
                )
            }
        )
    }
}
