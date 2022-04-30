package dev.mslalith.focuslauncher.ui.views.widgets

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mslalith.focuslauncher.data.models.ClockAlignment
import dev.mslalith.focuslauncher.data.models.Outcome
import dev.mslalith.focuslauncher.extensions.horizontalSpacer
import dev.mslalith.focuslauncher.extensions.verticalSpacer
import dev.mslalith.focuslauncher.ui.viewmodels.SettingsViewModel
import dev.mslalith.focuslauncher.ui.viewmodels.WidgetsViewModel
import dev.mslalith.focuslauncher.ui.views.widgets.AnalogClockPhase.BOTTOM
import dev.mslalith.focuslauncher.ui.views.widgets.AnalogClockPhase.BOTTOM_LEFT
import dev.mslalith.focuslauncher.ui.views.widgets.AnalogClockPhase.BOTTOM_RIGHT
import dev.mslalith.focuslauncher.ui.views.widgets.AnalogClockPhase.LEFT
import dev.mslalith.focuslauncher.ui.views.widgets.AnalogClockPhase.NONE
import dev.mslalith.focuslauncher.ui.views.widgets.AnalogClockPhase.RIGHT
import dev.mslalith.focuslauncher.ui.views.widgets.AnalogClockPhase.TOP
import dev.mslalith.focuslauncher.ui.views.widgets.AnalogClockPhase.TOP_LEFT
import dev.mslalith.focuslauncher.ui.views.widgets.AnalogClockPhase.TOP_RIGHT
import dev.mslalith.focuslauncher.ui.views.widgets.AnalogClockPhase.VERTICAL
import dev.mslalith.focuslauncher.utils.Constants.Defaults.DEFAULT_CLOCK_24_ANALOG_RADIUS
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ClockWidget(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel,
    widgetsViewModel: WidgetsViewModel,
    horizontalPadding: Dp,
    centerVertically: Boolean = false,
) {
    val context = LocalContext.current
    val currentTime by widgetsViewModel.currentTimeStateFlow.collectAsState()
    val showClock24 by settingsViewModel.showClock24StateFlow.collectAsState()
    val clockAlignment by settingsViewModel.clockAlignmentStateFlow.collectAsState()
    val clock24AnimationDuration by settingsViewModel.clock24AnimationDurationStateFlow.collectAsState()

    val horizontalBias by animateFloatAsState(
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        targetValue = when (clockAlignment) {
            ClockAlignment.START -> -1f
            ClockAlignment.CENTER -> 0f
            ClockAlignment.END -> 1f
        }
    )

    DisposableEffect(key1 = Unit) {
        widgetsViewModel.registerToTimeChange(context)
        onDispose { widgetsViewModel.unregisterToTimeChange(context) }
    }

    (currentTime as? Outcome.Success)?.value?.let { time ->
        Crossfade(
            targetState = showClock24,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding),
        ) {
            Column(
                horizontalAlignment = BiasAlignment.Horizontal(horizontalBias),
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (it) {
                    Clock24(
                        currentTime = time,
                        offsetAnimationSpec = tween(durationMillis = clock24AnimationDuration),
                        colorAnimationSpec = tween(durationMillis = clock24AnimationDuration),
                    )
                } else {
                    CurrentTime(
                        currentTime = time,
                        centerVertically = centerVertically,
                    )
                }
            }
        }
    }
}

@Composable
private fun CurrentTime(
    modifier: Modifier = Modifier,
    currentTime: String,
    centerVertically: Boolean = false,
) {
    val newModifier = if (centerVertically) modifier.fillMaxHeight() else modifier

    Box(
        modifier = newModifier,
        contentAlignment = if (centerVertically) Alignment.Center else Alignment.TopStart,
    ) {
        Crossfade(targetState = currentTime) {
            Text(
                text = it,
                style = TextStyle(
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                ),
            )
        }
    }
}

@Composable
fun Clock24(
    modifier: Modifier = Modifier,
    currentTime: String,
    analogClockRadius: Float = DEFAULT_CLOCK_24_ANALOG_RADIUS,
    analogClockSpacing: Dp = 4.dp,
    digitSpacing: Dp = 4.dp,
    handleColor: Color = MaterialTheme.colors.onBackground,
    handleWidth: Float = 4f,
    offsetAnimationSpec: AnimationSpec<Offset> = tween(durationMillis = 900),
    colorAnimationSpec: AnimationSpec<Color> = tween(durationMillis = 900),
) {
    val timeList = currentTime.toCharArray().filterNot { it == ':' }.map { it.toString().toInt() }

    Row(
        modifier = modifier.padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        timeList.forEachIndexed { index, digit ->
            DigitWithAnalogClocks(
                digit = Digit.ALL[digit],
                analogClockRadius = analogClockRadius,
                analogClockSpacing = analogClockSpacing,
                handleColor = handleColor,
                handleWidth = handleWidth,
                offsetAnimationSpec = offsetAnimationSpec,
                colorAnimationSpec = colorAnimationSpec,
            )
            if (index != timeList.size - 1) {
                digitSpacing.horizontalSpacer()
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
    colorAnimationSpec: AnimationSpec<Color>,
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
                    colorAnimationSpec = colorAnimationSpec,
                )
                analogClockSpacing.horizontalSpacer()
                AnalogClock(
                    radius = analogClockRadius,
                    analogClockPhase = list.last(),
                    handleColor = handleColor,
                    handleWidth = handleWidth,
                    offsetAnimationSpec = offsetAnimationSpec,
                    colorAnimationSpec = colorAnimationSpec,
                )
            }
            if (index != digit.analogHandles.size - 1) {
                analogClockSpacing.verticalSpacer()
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
    colorAnimationSpec: AnimationSpec<Color>,
) {
    val size = LocalDensity.current.run { radius.toDp() * 2 }
    val center = Offset(radius, radius)
    val disabledColor = handleColor.copy(alpha = 0.16f)

    fun offsetFromAngle(angle: Double) = Offset(
        x = radius * cos(Math.toRadians(angle)).toFloat(),
        y = radius * sin(Math.toRadians(angle)).toFloat(),
    ) + center

    val endFirst by animateOffsetAsState(
        targetValue = offsetFromAngle(analogClockPhase.first.angle),
        animationSpec = offsetAnimationSpec,
    )
    val endSecond by animateOffsetAsState(
        targetValue = offsetFromAngle(analogClockPhase.second.angle),
        animationSpec = offsetAnimationSpec,
    )
    val handleColorFirst by animateColorAsState(
        targetValue = if (analogClockPhase.first == AnalogClockHandlePhase.NONE) disabledColor else handleColor,
        animationSpec = colorAnimationSpec,
    )
    val handleColorSecond by animateColorAsState(
        targetValue = if (analogClockPhase.second == AnalogClockHandlePhase.NONE) disabledColor else handleColor,
        animationSpec = colorAnimationSpec,
    )

    Canvas(modifier = modifier.size(size)) {
        drawLine(
            color = handleColorFirst,
            start = center,
            end = endFirst,
            strokeWidth = handleWidth,
        )
        drawLine(
            color = handleColorSecond,
            start = center,
            end = endSecond,
            strokeWidth = handleWidth,
        )
    }
}

private enum class AnalogClockHandlePhase(val angle: Double) {
    NONE(angle = 135.0),
    TOP(angle = 270.0),
    RIGHT(angle = 0.0),
    BOTTOM(angle = 90.0),
    LEFT(angle = 180.0),
}

private enum class AnalogClockPhase(val first: AnalogClockHandlePhase, val second: AnalogClockHandlePhase) {
    NONE(first = AnalogClockHandlePhase.NONE, second = AnalogClockHandlePhase.NONE),
    TOP(first = AnalogClockHandlePhase.TOP, second = AnalogClockHandlePhase.TOP),
    RIGHT(first = AnalogClockHandlePhase.RIGHT, second = AnalogClockHandlePhase.RIGHT),
    BOTTOM(first = AnalogClockHandlePhase.BOTTOM, second = AnalogClockHandlePhase.BOTTOM),
    LEFT(first = AnalogClockHandlePhase.LEFT, second = AnalogClockHandlePhase.LEFT),
    VERTICAL(first = AnalogClockHandlePhase.TOP, second = AnalogClockHandlePhase.BOTTOM),
    TOP_LEFT(first = AnalogClockHandlePhase.TOP, second = AnalogClockHandlePhase.LEFT),
    TOP_RIGHT(first = AnalogClockHandlePhase.TOP, second = AnalogClockHandlePhase.RIGHT),
    BOTTOM_LEFT(first = AnalogClockHandlePhase.BOTTOM, second = AnalogClockHandlePhase.LEFT),
    BOTTOM_RIGHT(first = AnalogClockHandlePhase.BOTTOM, second = AnalogClockHandlePhase.RIGHT),
}

private data class Digit(val analogHandles: List<List<AnalogClockPhase>>) {
    companion object {
        val ZERO = Digit(
            analogHandles = listOf(
                listOf(BOTTOM_RIGHT, BOTTOM_LEFT),
                listOf(VERTICAL, VERTICAL),
                listOf(TOP_RIGHT, TOP_LEFT),
            )
        )
        val ONE = Digit(
            analogHandles = listOf(
                listOf(NONE, BOTTOM),
                listOf(NONE, VERTICAL),
                listOf(NONE, TOP),
            )
        )
        val TWO = Digit(
            analogHandles = listOf(
                listOf(RIGHT, BOTTOM_LEFT),
                listOf(BOTTOM_RIGHT, TOP_LEFT),
                listOf(TOP_RIGHT, LEFT),
            )
        )
        val THREE = Digit(
            analogHandles = listOf(
                listOf(RIGHT, BOTTOM_LEFT),
                listOf(RIGHT, TOP_LEFT),
                listOf(RIGHT, TOP_LEFT),
            )
        )
        val FOUR = Digit(
            analogHandles = listOf(
                listOf(BOTTOM, BOTTOM),
                listOf(TOP_RIGHT, TOP_LEFT),
                listOf(NONE, TOP),
            )
        )
        val FIVE = Digit(
            analogHandles = listOf(
                listOf(BOTTOM_RIGHT, LEFT),
                listOf(TOP_RIGHT, BOTTOM_LEFT),
                listOf(RIGHT, TOP_LEFT),
            )
        )
        val SIX = Digit(
            analogHandles = listOf(
                listOf(BOTTOM_RIGHT, LEFT),
                listOf(VERTICAL, BOTTOM_LEFT),
                listOf(TOP_RIGHT, TOP_LEFT),
            )
        )
        val SEVEN = Digit(
            analogHandles = listOf(
                listOf(RIGHT, BOTTOM_LEFT),
                listOf(NONE, VERTICAL),
                listOf(NONE, TOP),
            )
        )
        val EIGHT = Digit(
            analogHandles = listOf(
                listOf(BOTTOM_RIGHT, BOTTOM_LEFT),
                listOf(TOP_RIGHT, TOP_LEFT),
                listOf(TOP_RIGHT, TOP_LEFT),
            )
        )
        val NINE = Digit(
            analogHandles = listOf(
                listOf(BOTTOM_RIGHT, BOTTOM_LEFT),
                listOf(TOP_RIGHT, VERTICAL),
                listOf(RIGHT, TOP_LEFT),
            )
        )
        val ALL = listOf(ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE)
    }
}
