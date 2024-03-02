package dev.mslalith.focuslauncher.screens.launcher

import com.slack.circuit.runtime.CircuitUiState
import dev.mslalith.focuslauncher.feature.appdrawerpage.AppDrawerPageState
import dev.mslalith.focuslauncher.feature.homepage.HomePageState
import dev.mslalith.focuslauncher.feature.settingspage.SettingsPageState

data class LauncherState(
    val settingsPageState: SettingsPageState,
    val homePageState: HomePageState,
    val appDrawerPageState: AppDrawerPageState
) : CircuitUiState
