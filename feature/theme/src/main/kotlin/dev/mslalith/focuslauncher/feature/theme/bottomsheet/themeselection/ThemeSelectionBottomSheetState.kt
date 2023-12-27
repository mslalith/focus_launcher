package dev.mslalith.focuslauncher.feature.theme.bottomsheet.themeselection

import com.slack.circuit.runtime.CircuitUiState
import dev.mslalith.focuslauncher.core.model.Theme
import dev.mslalith.focuslauncher.feature.theme.model.ThemeWithIcon
import kotlinx.collections.immutable.ImmutableList

data class ThemeSelectionBottomSheetState(
    val currentTheme: Theme,
    val allThemes: ImmutableList<ThemeWithIcon>,
    val eventSink: (ThemeSelectionBottomSheetUiEvent) -> Unit
) : CircuitUiState
