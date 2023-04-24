package dev.mslalith.focuslauncher.core.ui.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@Composable
fun OnLifecycleEventChange(
    onEvent: (Lifecycle.Event) -> Unit
) {
    val updatedOnEvent by rememberUpdatedState(newValue = onEvent)
    val lifecycleOwner by rememberUpdatedState(newValue = LocalLifecycleOwner.current)

    DisposableEffect(key1 = lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            updatedOnEvent(event)
        }

        lifecycle.addObserver(observer = observer)
        onDispose { lifecycle.removeObserver(observer = observer) }
    }
}
