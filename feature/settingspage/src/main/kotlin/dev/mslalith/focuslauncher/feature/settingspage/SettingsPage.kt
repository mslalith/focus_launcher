package dev.mslalith.focuslauncher.feature.settingspage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.model.Screen
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.providers.LocalNavController
import dev.mslalith.focuslauncher.feature.settingspage.model.SettingsState
import dev.mslalith.focuslauncher.feature.settingspage.settingsitems.About
import dev.mslalith.focuslauncher.feature.settingspage.settingsitems.AppDrawer
import dev.mslalith.focuslauncher.feature.settingspage.settingsitems.ChangeTheme
import dev.mslalith.focuslauncher.feature.settingspage.settingsitems.EditFavorites
import dev.mslalith.focuslauncher.feature.settingspage.settingsitems.HideApps
import dev.mslalith.focuslauncher.feature.settingspage.settingsitems.IconPack
import dev.mslalith.focuslauncher.feature.settingspage.settingsitems.PullDownNotifications
import dev.mslalith.focuslauncher.feature.settingspage.settingsitems.SetAsDefaultLauncher
import dev.mslalith.focuslauncher.feature.settingspage.settingsitems.SettingsHeader
import dev.mslalith.focuslauncher.feature.settingspage.settingsitems.ToggleStatusBar
import dev.mslalith.focuslauncher.feature.settingspage.settingsitems.Widgets

@Composable
fun SettingsPage() {
    val navController = LocalNavController.current

    SettingsPageInternal(
        navigateTo = { navController.navigate(it.id) }
    )
}

@Composable
internal fun SettingsPageInternal(
    settingsPageViewModel: SettingsPageViewModel = hiltViewModel(),
    navigateTo: (Screen) -> Unit
) {
    val context = LocalContext.current
    val settingsState by settingsPageViewModel.settingsState.collectAsStateWithLifecycle()

    SettingsPageInternal(
        settingsState = settingsState,
        toggleStatusBarVisibility = settingsPageViewModel::toggleStatusBarVisibility,
        toggleNotificationShade = settingsPageViewModel::toggleNotificationShade,
        refreshIsDefaultLauncher = { settingsPageViewModel.refreshIsDefaultLauncher(context = context) },
        navigateTo = navigateTo
    )
}

@Composable
internal fun SettingsPageInternal(
    settingsState: SettingsState,
    toggleStatusBarVisibility: () -> Unit,
    toggleNotificationShade: () -> Unit,
    refreshIsDefaultLauncher: () -> Unit,
    navigateTo: (Screen) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState),
        verticalArrangement = Arrangement.Center
    ) {
        SettingsHeader()
        VerticalSpacer(spacing = 12.dp)

        ChangeTheme()

        EditFavorites { navigateTo(Screen.EditFavorites) }
        HideApps { navigateTo(Screen.HideApps) }

        ToggleStatusBar(
            showStatusBar = settingsState.showStatusBar,
            onClick = toggleStatusBarVisibility
        )
        PullDownNotifications(
            enableNotificationShade = settingsState.canDrawNotificationShade,
            onClick = toggleNotificationShade
        )

        IconPack(
            shouldShow = settingsState.showIconPack,
            onClick = { navigateTo(Screen.IconPack) }
        )

        AppDrawer()

        Widgets(
            navigateToCurrentPlace = { navigateTo(Screen.CurrentPlace) }
        )

        SetAsDefaultLauncher(
            isDefaultLauncher = settingsState.isDefaultLauncher,
            refreshIsDefaultLauncher = refreshIsDefaultLauncher
        )

        About { navigateTo(Screen.About) }

        VerticalSpacer(spacing = 12.dp)
    }
}
