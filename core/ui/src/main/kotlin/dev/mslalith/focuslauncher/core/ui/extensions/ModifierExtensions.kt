package dev.mslalith.focuslauncher.core.ui.extensions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

inline fun Modifier.modifyIf(
    predicate: () -> Boolean,
    block: Modifier.() -> Modifier
): Modifier = if (predicate()) this.then(other = block()) else this

fun Modifier.clickableNoRipple(
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier = composed {
    Modifier.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        enabled = enabled,
        onClick = onClick
    )
}

inline fun Modifier.onSwipeDown(
    enabled: Boolean = true,
    crossinline action: () -> Unit
) = composed {
    val velocityThreshold = 600f
    var yStart = 0f
    var yDrag = 0f

    this then Modifier.draggable(
        enabled = enabled,
        orientation = Orientation.Vertical,
        onDragStarted = {
            yStart = it.y
            yDrag = yStart
        },
        state = rememberDraggableState { delta ->
            yDrag += delta
        },
        onDragStopped = { velocity ->
            if (yStart < yDrag && velocity > velocityThreshold) {
                action()
            }
        }
    )
}
