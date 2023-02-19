package dev.mslalith.focuslauncher.feature.settingspage.shared

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun SettingsExpandableItem(
    text: String,
    contentPadding: Dp = 40.dp,
    bottomPadding: Dp = 8.dp,
    curvyLineSurroundPadding: PaddingValues? = null,
    content: @Composable (() -> Unit) -> Unit
) {
    val durationMillis = 350
    val coroutineScope = rememberCoroutineScope()
    var isExpanded by remember { mutableStateOf(false) }

    val curvyLineSurroundPaddingValues = curvyLineSurroundPadding ?: PaddingValues(start = 4.dp, top = 4.dp, bottom = 4.dp)
    val dividerColor = MaterialTheme.colors.onBackground
    val dividerWidth = 3f

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            SettingsItem(
                text = text,
                onClick = { isExpanded = !isExpanded }
            )
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = durationMillis)
                ) + expandVertically(
                    animationSpec = tween(durationMillis = durationMillis)
                ),
                exit = fadeOut(
                    animationSpec = tween(durationMillis = durationMillis)
                ) + shrinkVertically(
                    animationSpec = tween(durationMillis = durationMillis)
                ),
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(
                        start = contentPadding,
                        end = ITEM_PADDING,
                        bottom = bottomPadding
                    )
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = dividerWidth.dp * 2.5f)
                        .settingsContentCurve(
                            dividerColor = dividerColor,
                            dividerWidth = dividerWidth
                        )
                        .padding(start = dividerWidth.dp)
                        .padding(vertical = dividerWidth.dp)
                        .padding(paddingValues = curvyLineSurroundPaddingValues)
                ) {
                    content {
                        coroutineScope.launch {
                            delay(timeMillis = durationMillis + 100L)
                            isExpanded = false
                        }
                    }
                }
            }
        }
    }
}

private fun Modifier.settingsContentCurve(
    dividerColor: Color,
    dividerWidth: Float = 3f,
    dividerCurveOffset: Float = 24f,
    horizontalWidth: Float = 36f
) = this then Modifier.drawWithCache {
    onDrawBehind {
        val lineHalf = dividerWidth / 2
        val yCurveEnd = size.height - dividerCurveOffset
        val xCurveStart = dividerCurveOffset + lineHalf
        val xEnd = xCurveStart + horizontalWidth
        val yEnd = size.height - lineHalf
        val path = Path().apply {
            moveTo(xEnd, lineHalf)
            lineTo(xCurveStart, lineHalf)
            quadraticBezierTo(lineHalf, lineHalf, lineHalf, dividerCurveOffset + lineHalf)
            lineTo(lineHalf, yCurveEnd - lineHalf)
            quadraticBezierTo(lineHalf, yEnd, xCurveStart, yEnd)
            lineTo(xEnd, yEnd)
        }
        drawPath(
            path = path,
            color = dividerColor,
            style = Stroke(
                width = dividerWidth,
                cap = StrokeCap.Round
            )
        )
    }
}
