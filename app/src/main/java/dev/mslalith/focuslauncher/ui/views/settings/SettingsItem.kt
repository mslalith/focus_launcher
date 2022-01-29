package dev.mslalith.focuslauncher.ui.views.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val ITEM_PADDING = 28.dp

@Composable
fun SettingsExpandableItem(
    text: String,
    contentPadding: Dp = 40.dp,
    bottomPadding: Dp = 8.dp,
    curvyLineSurroundPadding: PaddingValues? = null,
    content: @Composable (() -> Unit) -> Unit,
) {
    val durationMillis = 350
    val coroutineScope = rememberCoroutineScope()
    var isExpanded by remember { mutableStateOf(false) }

    val curvyLineSurroundPaddingValues = curvyLineSurroundPadding ?: PaddingValues(start = 4.dp, top = 4.dp, bottom = 4.dp)
    val dividerColor = MaterialTheme.colors.onBackground
    val dividerWidth = 3f

    Box(
        modifier = Modifier.fillMaxWidth(),
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
                        bottom = bottomPadding,
                    ),
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

@Composable
fun SettingsGridItem(
    modifier: Modifier = Modifier,
    text: String,
    showIcon: Boolean = false,
    horizontalPadding: Dp? = null,
    verticalPadding: Dp = 8.dp,
    icon: ImageVector? = null,
    contentDescription: String? = null,
    onClick: () -> Unit,
) {
    val density = LocalDensity.current
    val usableHorizontalPadding = horizontalPadding ?: ITEM_PADDING

    val color = MaterialTheme.colors.onBackground
    var contentHeight by remember { mutableStateOf(0.dp) }
    val iconModifier = Modifier.size(contentHeight)


    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp)
            .clip(shape = MaterialTheme.shapes.small)
            .clickable { onClick() }
            .padding(
                horizontal = usableHorizontalPadding,
                vertical = verticalPadding,
            )
            .onSizeChanged {
                density.run { contentHeight = it.height.toDp() }
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showIcon) {
            if (icon != null) {
                Box(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .then(iconModifier),
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = contentDescription ?: text,
                        tint = color,
                    )
                }
            } else {
                Spacer(iconModifier)
            }
        }
        Text(
            text = text,
            style = TextStyle(color = color)
        )
        if (showIcon) {
            Spacer(iconModifier)
        }
    }
}

@Composable
fun LoadingSettingsItem(
    text: String,
    isLoading: Boolean,
    horizontalPadding: Dp? = null,
    afterLeadingPadding: Dp = 20.dp,
    onClick: (() -> Unit)? = null,
) {
    val density = LocalDensity.current
    var contentHeight by remember { mutableStateOf(0.dp) }

    SettingsItem(
        text = text,
        horizontalPadding = horizontalPadding,
        afterLeadingPadding = afterLeadingPadding,
        onClick = if (isLoading) null else onClick,
        leading = if (isLoading) {
            {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.onBackground,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(size = contentHeight / 2)
                )
            }
        } else null,
        modifier = Modifier.onSizeChanged {
            contentHeight = density.run { it.height.toDp() }
        }
    )
}

@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    text: String,
    horizontalPadding: Dp? = null,
    verticalPadding: Dp = 12.dp,
    afterLeadingPadding: Dp = 20.dp,
    leading: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    val usableHorizontalPadding = (horizontalPadding ?: ITEM_PADDING) * .75f

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = usableHorizontalPadding)
            .clip(shape = MaterialTheme.shapes.small)
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(
                horizontal = usableHorizontalPadding,
                vertical = verticalPadding,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AnimatedVisibility(visible = leading != null) {
            Box(
                modifier = Modifier.padding(end = afterLeadingPadding),
            ) {
                leading?.invoke()
            }
        }
        Crossfade(targetState = text) {
            Text(
                text = it,
                style = TextStyle(color = MaterialTheme.colors.onBackground)
            )
        }
    }
}

private fun Modifier.settingsContentCurve(
    dividerColor: Color,
    dividerWidth: Float = 3f,
    dividerCurveOffset: Float = 24f,
    horizontalWidth: Float = 36f,
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

