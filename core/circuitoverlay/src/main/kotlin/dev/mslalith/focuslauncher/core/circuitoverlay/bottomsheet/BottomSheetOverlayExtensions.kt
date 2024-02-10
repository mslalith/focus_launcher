package dev.mslalith.focuslauncher.core.circuitoverlay.bottomsheet

import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.overlay.OverlayHost
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.PopResult
import com.slack.circuit.runtime.screen.Screen
import dev.mslalith.focuslauncher.core.circuitoverlay.OverlayResultScreen
import dev.mslalith.focuslauncher.core.screens.BottomSheetScreen

fun <T> Navigator.overlayHostResult(result: T) {
    goTo(OverlayResultScreen(result = result))
}

suspend fun OverlayHost.showBottomSheet(
    screen: BottomSheetScreen<Unit>
): Unit = show(
    BottomSheetOverlay(
        state = Unit,
        onDismiss = {}
    ) { _, navigator ->
        CircuitContent(
            screen = screen,
            onNavEvent = { event ->
                when (event) {
                    is NavEvent.Pop -> navigator.finish(Unit)
                    else -> Unit
                }
            }
        )
    }
)

suspend fun <T : Any> OverlayHost.showBottomSheetWithResult(
    screen: BottomSheetScreen<T>
): T? = show(
    BottomSheetOverlay<Unit, OverlayResultScreen<T>>(
        state = Unit,
        onDismiss = { OverlayResultScreen() }
    ) { _, navigator ->
        CircuitContent(
            screen = screen,
            navigator = object : Navigator {
                override fun goTo(screen: Screen) {
                    if (screen is OverlayResultScreen<*>) {
                        @Suppress("UNCHECKED_CAST")
                        navigator.finish(
                            result = screen as? OverlayResultScreen<T> ?: error("Incorrect result type for the current overlay")
                        )
                    } else {
                        error("Navigation directly to other screens from overlays is not supported")
                    }
                }

                override fun peek(): Screen? = null

                override fun peekBackStack(): List<Screen> = emptyList()

                override fun pop(result: PopResult?): Screen? {
                    navigator.finish(OverlayResultScreen())
                    return null
                }

                override fun resetRoot(newRoot: Screen, saveState: Boolean, restoreState: Boolean): List<Screen> {
                    error("Operation not allowed in overlays")
                }
            }
        )
    }
).result
