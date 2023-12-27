package dev.mslalith.focuslauncher.core.circuitoverlay

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.internal.BackHandler
import com.slack.circuit.overlay.Overlay
import com.slack.circuit.overlay.OverlayHost
import com.slack.circuit.overlay.OverlayNavigator
import com.slack.circuit.runtime.screen.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class BottomSheetOverlay<S : Any, R : Any>(
    private val state: S,
    private val onDismiss: () -> R,
    private val content: @Composable (S, OverlayNavigator<R>) -> Unit
) : Overlay<R> {
    @Composable
    override fun Content(navigator: OverlayNavigator<R>) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val scope = rememberCoroutineScope()

        BackHandler(enabled = sheetState.isVisible) {
            scope
                .launch { sheetState.hide() }
                .invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        navigator.finish(onDismiss())
                    }
                }
        }

        ModalBottomSheet(
            modifier = Modifier.fillMaxWidth(),
            content = {
                content(state) { result ->
                    scope.launch {
                        try {
                            sheetState.hide()
                        } finally {
                            navigator.finish(result)
                        }
                    }
                }
            },
            sheetState = sheetState,
            onDismissRequest = { navigator.finish(onDismiss()) }
        )

        LaunchedEffect(Unit) { sheetState.show() }
    }
}

suspend fun OverlayHost.showBottomSheet(
    screen: Screen
): Unit = show(
    BottomSheetOverlay(
        state = Unit,
        onDismiss = {}
    ) { _, navigator ->
        CircuitContent(
            screen = screen,
            onNavEvent = { event ->
                when (event) {
                    NavEvent.Pop -> navigator.finish(Unit)
                    else -> Unit
                }
            }
        )
    }
)
