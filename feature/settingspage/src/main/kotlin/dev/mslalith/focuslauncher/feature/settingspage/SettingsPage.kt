package dev.mslalith.focuslauncher.feature.settingspage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.mslalith.focuslauncher.core.model.Screen
import dev.mslalith.focuslauncher.core.model.WidgetType
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.managers.LauncherViewManager
import dev.mslalith.focuslauncher.core.ui.providers.LocalLauncherViewManager
import dev.mslalith.focuslauncher.core.ui.providers.LocalNavController
import dev.mslalith.focuslauncher.feature.clock24.settings.ClockSettingsSheet
import dev.mslalith.focuslauncher.feature.lunarcalendar.model.LunarPhaseSettingsProperties
import dev.mslalith.focuslauncher.feature.lunarcalendar.settings.LunarPhaseSettingsSheet
import dev.mslalith.focuslauncher.feature.quoteforyou.settings.QuotesSettingsSheet
import dev.mslalith.focuslauncher.feature.settingspage.settingsitems.AppDrawer
import dev.mslalith.focuslauncher.feature.settingspage.settingsitems.ChangeTheme
import dev.mslalith.focuslauncher.feature.settingspage.settingsitems.EditFavorites
import dev.mslalith.focuslauncher.feature.settingspage.settingsitems.HideApps
import dev.mslalith.focuslauncher.feature.settingspage.settingsitems.PullDownNotifications
import dev.mslalith.focuslauncher.feature.settingspage.settingsitems.SetAsDefaultLauncher
import dev.mslalith.focuslauncher.feature.settingspage.settingsitems.SettingsHeader
import dev.mslalith.focuslauncher.feature.settingspage.settingsitems.ToggleStatusBar
import dev.mslalith.focuslauncher.feature.settingspage.settingsitems.Widgets

@Composable
fun SettingsPage() {
    val navController = LocalNavController.current

    SettingsPage(
        themeViewModel = hiltViewModel(),
        settingsViewModel = hiltViewModel(),
        navigateTo = { navController.navigate(it.id) }
    )
}

@Composable
internal fun SettingsPage(
    themeViewModel: ThemeViewModel,
    settingsViewModel: SettingsViewModel,
    navigateTo: (Screen) -> Unit
) {
    val context = LocalContext.current
    val viewManager = LocalLauncherViewManager.current

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center
    ) {
        SettingsHeader()
        VerticalSpacer(spacing = 12.dp)

        ChangeTheme(
            currentTheme = themeViewModel.currentThemeStateFlow.collectAsState().value,
            changeTheme = themeViewModel::changeTheme
        )

        EditFavorites { navigateTo(Screen.EditFavorites) }
        HideApps { navigateTo(Screen.HideApps) }

        ToggleStatusBar(
            showStatusBar = settingsViewModel.statusBarVisibilityStateFlow.collectAsState().value,
            onClick = settingsViewModel::toggleStatusBarVisibility
        )
        PullDownNotifications(
            enableNotificationShade = settingsViewModel.notificationShadeStateFlow.collectAsState().value,
            onClick = settingsViewModel::toggleNotificationShade
        )

        AppDrawer {
            viewManager.showBottomSheet {
                AppDrawerSettingsSheet(settingsViewModel = settingsViewModel)
            }
        }

        Widgets(viewManager = viewManager)

        SetAsDefaultLauncher(
            isDefaultLauncher = settingsViewModel.isDefaultLauncherStateFlow.collectAsState().value,
            refreshIsDefaultLauncher = { settingsViewModel.refreshIsDefaultLauncher(context) }
        )

        VerticalSpacer(spacing = 12.dp)
    }
}

@Composable
private fun Widgets(
    viewManager: LauncherViewManager
) {
    val navController = LocalNavController.current

    Widgets { widgetType ->
        viewManager.showBottomSheet {
            when (widgetType) {
                WidgetType.CLOCK -> {
                    viewManager.showBottomSheet { ClockSettingsSheet() }
                }
                WidgetType.LUNAR_PHASE -> {
                    viewManager.showBottomSheet {
                        LunarPhaseSettingsSheet(
                            properties = LunarPhaseSettingsProperties(
                                navigateToPickPlaceForLunarPhase = {
                                    navController.navigate(Screen.PickPlaceForLunarPhase.id)
                                }
                            )
                        )
                    }
                }
                WidgetType.QUOTES -> {
                    viewManager.showBottomSheet { QuotesSettingsSheet() }
                }
            }
        }
    }
}
