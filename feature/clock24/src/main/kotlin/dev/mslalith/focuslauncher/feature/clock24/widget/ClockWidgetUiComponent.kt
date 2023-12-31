package dev.mslalith.focuslauncher.feature.clock24.widget

import android.content.Intent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import dev.mslalith.focuslauncher.core.model.ClockAlignment
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.testSemantics
import dev.mslalith.focuslauncher.core.ui.effects.OnLifecycleEventChange
import dev.mslalith.focuslauncher.core.ui.effects.SystemBroadcastReceiver
import dev.mslalith.focuslauncher.core.ui.extensions.clickableNoRipple
import dev.mslalith.focuslauncher.core.ui.extensions.modifyIf
import dev.mslalith.focuslauncher.feature.clock24.widget.ui.Clock24
import dev.mslalith.focuslauncher.feature.clock24.widget.ui.CurrentTime
import dev.mslalith.focuslauncher.feature.clock24.utils.TestTags

@Composable
fun ClockWidgetUiComponent(
    state: ClockWidgetUiComponentState,
    horizontalPadding: Dp,
    modifier: Modifier = Modifier,
    verticalPadding: Dp = 0.dp,
    onClick: (() -> Unit)? = null,
    contentColor: Color = MaterialTheme.colorScheme.onSurface
) {
    // Need to extract the eventSink out to a local val, so that the Compose Compiler
    // treats it as stable. See: https://issuetracker.google.com/issues/256100927
    val eventSink = state.eventSink

    ClockWidgetUiComponent(
        modifier = modifier,
        state = state,
        refreshTime = { eventSink(ClockWidgetUiComponentUiEvent.RefreshTime) },
        horizontalPadding = horizontalPadding,
        verticalPadding = verticalPadding,
        onClick = onClick,
        contentColor = contentColor
    )
}

@Composable
private fun ClockWidgetUiComponent(
    state: ClockWidgetUiComponentState,
    refreshTime: () -> Unit,
    horizontalPadding: Dp,
    modifier: Modifier = Modifier,
    verticalPadding: Dp = 0.dp,
    onClick: (() -> Unit)? = null,
    contentColor: Color = MaterialTheme.colorScheme.onSurface
) {
    val updatedRefreshTime by rememberUpdatedState(newValue = refreshTime)

    val horizontalBias by animateFloatAsState(
        label = "Clock Alignment",
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        targetValue = when (state.clockAlignment) {
            ClockAlignment.START -> -1f
            ClockAlignment.CENTER -> 0f
            ClockAlignment.END -> 1f
        }
    )

    SystemBroadcastReceiver(systemAction = Intent.ACTION_TIME_TICK) {
        updatedRefreshTime()
    }

    OnLifecycleEventChange { event ->
        if (event == Lifecycle.Event.ON_RESUME) updatedRefreshTime()
    }

    val clickModifier = Modifier.modifyIf(predicate = { onClick != null }) {
        clickableNoRipple { onClick?.invoke() }
    }

    Crossfade(
        label = "Show Clock 24 CrossFade",
        targetState = state.showClock24,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding)
    ) { showClock24 ->
        Column(
            horizontalAlignment = BiasAlignment.Horizontal(bias = horizontalBias),
            modifier = Modifier
                .fillMaxWidth()
                .testSemantics(tag = TestTags.TAG_CLOCK_COLUMN) {
                    testBiasAlignment(biasAlignment = BiasAlignment.Horizontal(bias = horizontalBias))
                }
        ) {
            if (showClock24) {
                Clock24(
                    modifier = clickModifier,
                    currentTime = state.currentTime,
                    handleColor = contentColor,
                    offsetAnimationSpec = tween(durationMillis = state.clock24AnimationDuration),
                    colorAnimationSpec = tween(durationMillis = state.clock24AnimationDuration)
                )
            } else {
                CurrentTime(
                    currentTime = state.currentTime,
                    contentColor = contentColor,
                    modifier = Modifier
                        .then(other = clickModifier)
                        .padding(vertical = verticalPadding)
                )
            }
        }
    }
}
