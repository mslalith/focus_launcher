package dev.mslalith.focuslauncher.core.screens

import com.slack.circuit.runtime.screen.Screen
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerItem
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

abstract class BottomSheetScreen<T> : Screen

@Parcelize
data object ThemeSelectionBottomSheetScreen : BottomSheetScreen<Unit>()

@Parcelize
data object AppDrawerSettingsBottomSheetScreen : BottomSheetScreen<Unit>()

@Parcelize
data object ClockWidgetSettingsBottomSheetScreen : BottomSheetScreen<Unit>()

@Parcelize
data object QuoteWidgetSettingsBottomSheetScreen : BottomSheetScreen<Unit>()

@Parcelize
data object LunarPhaseWidgetSettingsBottomSheetScreen : BottomSheetScreen<LunarPhaseWidgetSettingsBottomSheetScreen.Result>() {
    sealed interface Result {
        data class PopAndGoto(val screen: Screen) : Result
    }
}

@Parcelize
data object LunarPhaseDetailsBottomSheetScreen : BottomSheetScreen<Unit>()

@Parcelize
data class AppMoreOptionsBottomSheetScreen(val appDrawerItem: @RawValue AppDrawerItem) : BottomSheetScreen<Unit>()

@Parcelize
data class UpdateAppDisplayNameBottomSheetScreen(val app: @RawValue App) : BottomSheetScreen<Unit>()
