package dev.mslalith.focuslauncher.core.screens

import com.slack.circuit.runtime.screen.Screen
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerItem
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data object ThemeSelectionBottomSheetScreen : Screen

@Parcelize
data object AppDrawerSettingsBottomSheetScreen : Screen

@Parcelize
data object ClockWidgetSettingsBottomSheetScreen : Screen

@Parcelize
data object QuoteWidgetSettingsBottomSheetScreen : Screen

@Parcelize
data object LunarPhaseWidgetSettingsBottomSheetScreen : Screen

@Parcelize
data object LunarPhaseDetailsBottomSheetScreen : Screen

@Parcelize
data class AppMoreOptionsBottomSheetScreen(val appDrawerItem: @RawValue AppDrawerItem) : Screen
