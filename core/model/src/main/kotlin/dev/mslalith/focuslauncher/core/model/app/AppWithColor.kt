package dev.mslalith.focuslauncher.core.model.app

import android.graphics.Color
import androidx.compose.runtime.Immutable

@Immutable
data class AppWithColor(
    val app: App,
    val color: Color?
)
