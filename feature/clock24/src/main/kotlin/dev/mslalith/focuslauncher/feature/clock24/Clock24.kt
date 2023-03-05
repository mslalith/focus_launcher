package dev.mslalith.focuslauncher.feature.clock24

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.DEFAULT_CLOCK_24_ANALOG_RADIUS
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.testSemantics
import dev.mslalith.focuslauncher.core.ui.HorizontalSpacer
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.feature.clock24.model.AnalogClockHandlePhase
import dev.mslalith.focuslauncher.feature.clock24.model.AnalogClockPhase
import dev.mslalith.focuslauncher.feature.clock24.model.Digit
import dev.mslalith.focuslauncher.feature.clock24.utils.TestTags
import kotlin.math.cos
import kotlin.math.sin

@Composable
internal fun Clock24(
    modifier: Modifier = Modifier,
    currentTime: String,
    analogClockRadius: Float = DEFAULT_CLOCK_24_ANALOG_RADIUS,
    analogClockSpacing: Dp = 4.dp,
    digitSpacing: Dp = 4.dp,
    handleColor: Color = MaterialTheme.colors.onBackground,
    handleWidth: Float = 4f,
    offsetAnimationSpec: AnimationSpec<Offset> = tween(durationMillis = 900),
    colorAnimationSpec: AnimationSpec<Color> = tween(durationMillis = 900)
) {
    val timeList = remember(key1 = currentTime) {
        currentTime.toCharArray().filterNot { it == ':' }.map { it.toString().toInt() }
    }

    Row(
        modifier = modifier
            .padding(vertical = 12.dp)
            .testSemantics(tag = TestTags.TAG_CLOCK24),
        horizontalArrangement = Arrangement.Center
    ) {
        timeList.forEachIndexed { index, digit ->
            DigitWithAnalogClocks(
                digit = Digit.ALL[digit],
                analogClockRadius = analogClockRadius,
                analogClockSpacing = analogClockSpacing,
                handleColor = handleColor,
                handleWidth = handleWidth,
                offsetAnimationSpec = offsetAnimationSpec,
                colorAnimationSpec = colorAnimationSpec
            )
            if (index != timeList.lastIndex) {
                HorizontalSpacer(spacing = digitSpacing)
            }
        }
    }
}

@Composable
private fun DigitWithAnalogClocks(
    digit: Digit,
    analogClockRadius: Float,
    analogClockSpacing: Dp,
    handleColor: Color,
    handleWidth: Float,
    offsetAnimationSpec: AnimationSpec<Offset>,
    colorAnimationSpec: AnimationSpec<Color>
) {
    Column {
        digit.analogHandles.forEachIndexed { index, list ->
            Row {
                AnalogClock(
                    radius = analogClockRadius,
                    analogClockPhase = list.first(),
                    handleColor = handleColor,
                    handleWidth = handleWidth,
                    offsetAnimationSpec = offsetAnimationSpec,
                    colorAnimationSpec = colorAnimationSpec
                )
                HorizontalSpacer(spacing = analogClockSpacing)
                AnalogClock(
                    radius = analogClockRadius,
                    analogClockPhase = list.last(),
                    handleColor = handleColor,
                    handleWidth = handleWidth,
                    offsetAnimationSpec = offsetAnimationSpec,
                    colorAnimationSpec = colorAnimationSpec
                )
            }
            if (index != digit.analogHandles.lastIndex) {
                VerticalSpacer(spacing = analogClockSpacing)
            }
        }
    }
}

@Composable
private fun AnalogClock(
    modifier: Modifier = Modifier,
    radius: Float,
    analogClockPhase: AnalogClockPhase,
    handleColor: Color,
    handleWidth: Float,
    offsetAnimationSpec: AnimationSpec<Offset>,
    colorAnimationSpec: AnimationSpec<Color>
) {
    val size = LocalDensity.current.run { radius.toDp() * 2 }
    val center = Offset(radius, radius)
    val disabledColor = handleColor.copy(alpha = 0.16f)

    fun offsetFromAngle(angle: Double) = Offset(
        x = radius * cos(Math.toRadians(angle)).toFloat(),
        y = radius * sin(Math.toRadians(angle)).toFloat()
    ) + center

    val endFirst by animateOffsetAsState(
        label = "End first offset",
        targetValue = offsetFromAngle(analogClockPhase.first.angle),
        animationSpec = offsetAnimationSpec
    )
    val endSecond by animateOffsetAsState(
        label = "End second offset",
        targetValue = offsetFromAngle(analogClockPhase.second.angle),
        animationSpec = offsetAnimationSpec
    )
    val handleColorFirst by animateColorAsState(
        label = "Handle color first",
        targetValue = if (analogClockPhase.first == AnalogClockHandlePhase.NONE) disabledColor else handleColor,
        animationSpec = colorAnimationSpec
    )
    val handleColorSecond by animateColorAsState(
        label = "handle color second",
        targetValue = if (analogClockPhase.second == AnalogClockHandlePhase.NONE) disabledColor else handleColor,
        animationSpec = colorAnimationSpec
    )

    Canvas(modifier = modifier.size(size)) {
        drawLine(
            color = handleColorFirst,
            start = center,
            end = endFirst,
            strokeWidth = handleWidth
        )
        drawLine(
            color = handleColorSecond,
            start = center,
            end = endSecond,
            strokeWidth = handleWidth
        )
    }
}
