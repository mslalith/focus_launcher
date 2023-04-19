package dev.mslalith.focuslauncher.core.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun DotWaveLoader(
    modifier: Modifier = Modifier,
    size: Dp = 4.dp,
    color: Color = MaterialTheme.colorScheme.onSurface,
    spacing: Dp = 4.dp,
    distance: Dp = size / 2,
    paddingValues: PaddingValues = PaddingValues()
) {
    val density = LocalDensity.current

    val distancePx = remember(key1 = distance) {
        with(density) { distance.toPx() }
    }

    val animatables = listOf(
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) }
    )

    animatables.forEachIndexed { index, animatable ->
        LaunchedEffect(key1 = animatable) {
            delay(timeMillis = 150L * index)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    repeatMode = RepeatMode.Restart,
                    animation = keyframes {
                        durationMillis = 800
                        0f at 0 with LinearEasing
                        0.5f at 150 with LinearEasing
                        1f at 300 with LinearEasing
                        0.5f at 450 with LinearEasing
                        0f at 600 with LinearEasing
                        0f at 800 with LinearEasing
                    }
                )
            )
        }
    }

    Row(
        modifier = modifier
            .padding(paddingValues = paddingValues)
            .height(height = size + distance),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        Row {
            animatables.forEachIndexed { index, animatable ->
                Box(
                    modifier = Modifier
                        .size(size = size)
                        .graphicsLayer { translationY = -animatable.value * distancePx }
                        .background(color = color, shape = CircleShape)
                )
                if (index != animatables.lastIndex) HorizontalSpacer(spacing = spacing)
            }
        }
    }
}

@Preview
@Composable
private fun PreviewDotWaveLoader() {
    MaterialTheme {
        DotWaveLoader()
    }
}
