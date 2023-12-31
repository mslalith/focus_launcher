package dev.mslalith.focuslauncher.feature.theme.bottomsheet.themeselection

import com.slack.circuit.runtime.CircuitUiEvent
import dev.mslalith.focuslauncher.core.model.Theme

sealed interface ThemeSelectionBottomSheetUiEvent : CircuitUiEvent {
    data class SelectedTheme(val theme: Theme?) : ThemeSelectionBottomSheetUiEvent
}
