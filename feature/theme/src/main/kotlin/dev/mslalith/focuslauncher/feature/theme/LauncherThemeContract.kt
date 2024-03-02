package dev.mslalith.focuslauncher.feature.theme

import com.slack.circuit.runtime.CircuitUiState
import dev.mslalith.focuslauncher.core.model.Theme

data class LauncherThemeState(
    val theme: Theme
) : CircuitUiState
