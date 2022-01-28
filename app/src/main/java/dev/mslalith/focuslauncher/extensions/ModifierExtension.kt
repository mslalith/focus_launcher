package dev.mslalith.focuslauncher.extensions

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

inline fun Modifier.modifyIf(
    predicate: () -> Boolean,
    block: Modifier.() -> Modifier,
): Modifier = if (predicate()) this.then(block()) else this

inline fun Modifier.onSwipeDown(
    enabled: Boolean = true,
    crossinline action: () -> Unit,
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
