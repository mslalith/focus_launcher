package dev.mslalith.focuslauncher.core.circuitoverlay

import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class OverlayResultScreen<T>(
    val result: @RawValue T? = null
) : Screen
