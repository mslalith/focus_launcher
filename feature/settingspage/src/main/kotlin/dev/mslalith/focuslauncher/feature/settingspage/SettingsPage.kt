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
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.model.Screen
import dev.mslalith.focuslauncher.core.screens.SettingsPageScreen
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.providers.LocalLauncherViewManager
import dev.mslalith.focuslauncher.core.ui.providers.LocalNavController
import dev.mslalith.focuslauncher.feature.clock24.settings.ClockSettingsSheet
import dev.mslalith.focuslauncher.feature.lunarcalendar.model.LunarPhaseSettingsProperties
import dev.mslalith.focuslauncher.feature.lunarcalendar.settings.LunarPhaseSettingsSheet
import dev.mslalith.focuslauncher.feature.quoteforyou.settings.QuotesSettingsSheet
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
import dev.mslalith.focuslauncher.feature.theme.ThemeSelectionSheet

@CircuitInject(SettingsPageScreen::class, SingletonComponent::class)
@Composable
fun SettingsPage(
    state: SettingsPageState,
    modifier: Modifier = Modifier
) {
    // Need to extract the eventSink out to a local val, so that the Compose Compiler
    // treats it as stable. See: https://issuetracker.google.com/issues/256100927
    val eventSink = state.eventSink

    val context = LocalContext.current

    SettingsPageInternal(
        modifier = modifier,
        settingsState = SettingsState(
            state.showStatusBar,
            state.canDrawNotificationShade,
            state.showIconPack,
            state.isDefaultLauncher
        ),
        onThemeClick = {},
        onToggleStatusBarVisibility = { eventSink(SettingsPageUiEvent.ToggleStatusBarVisibility) },
        onToggleNotificationShade = { eventSink(SettingsPageUiEvent.ToggleNotificationShade) },
        onAppDrawerClick = {},
        onClockWidgetClick = {},
        onLunarPhaseWidgetClick = {},
        onQuotesWidgetClick = {},
        onRefreshIsDefaultLauncher = { eventSink(SettingsPageUiEvent.RefreshIsDefaultLauncher(context = context)) },
        navigateTo = {}
    )
}

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
    val viewManager = LocalLauncherViewManager.current

    val settingsState by settingsPageViewModel.settingsState.collectAsStateWithLifecycle()

    fun onThemeClick() {
        viewManager.showBottomSheet {
            ThemeSelectionSheet(closeBottomSheet = viewManager::hideBottomSheet)
        }
    }

    fun onAppDrawerClick() {
        viewManager.showBottomSheet {
            AppDrawerSettingsSheet()
        }
    }

    fun onClockWidgetClick() {
        viewManager.showBottomSheet {
            ClockSettingsSheet()
        }
    }

    fun onLunarPhaseWidgetClick() {
        viewManager.showBottomSheet {
            LunarPhaseSettingsSheet(
                properties = LunarPhaseSettingsProperties(
                    navigateToCurrentPlace = {
                        viewManager.hideBottomSheet()
                        navigateTo(Screen.CurrentPlace)
                    }
                )
            )
        }
    }

    fun onQuotesWidgetClick() {
        viewManager.showBottomSheet {
            QuotesSettingsSheet()
        }
    }

    SettingsPageInternal(
        settingsState = settingsState,
        onThemeClick = ::onThemeClick,
        onToggleStatusBarVisibility = settingsPageViewModel::toggleStatusBarVisibility,
        onToggleNotificationShade = settingsPageViewModel::toggleNotificationShade,
        onAppDrawerClick = ::onAppDrawerClick,
        onClockWidgetClick = ::onClockWidgetClick,
        onLunarPhaseWidgetClick = ::onLunarPhaseWidgetClick,
        onQuotesWidgetClick = ::onQuotesWidgetClick,
        onRefreshIsDefaultLauncher = { settingsPageViewModel.refreshIsDefaultLauncher(context = context) },
        navigateTo = navigateTo
    )
}

@Composable
internal fun SettingsPageInternal(
    settingsState: SettingsState,
    onThemeClick: () -> Unit,
    onToggleStatusBarVisibility: () -> Unit,
    onToggleNotificationShade: () -> Unit,
    onAppDrawerClick: () -> Unit,
    onClockWidgetClick: () -> Unit,
    onLunarPhaseWidgetClick: () -> Unit,
    onQuotesWidgetClick: () -> Unit,
    onRefreshIsDefaultLauncher: () -> Unit,
    navigateTo: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState),
        verticalArrangement = Arrangement.Center
    ) {
        SettingsHeader()
        VerticalSpacer(spacing = 12.dp)

        ChangeTheme(onClick = onThemeClick)

        EditFavorites { navigateTo(Screen.EditFavorites) }
        HideApps { navigateTo(Screen.HideApps) }

        ToggleStatusBar(
            showStatusBar = settingsState.showStatusBar,
            onClick = onToggleStatusBarVisibility
        )
        PullDownNotifications(
            enableNotificationShade = settingsState.canDrawNotificationShade,
            onClick = onToggleNotificationShade
        )

        IconPack(
            shouldShow = settingsState.showIconPack,
            onClick = { navigateTo(Screen.IconPack) }
        )

        AppDrawer(onClick = onAppDrawerClick)

        Widgets(
            onClockWidgetClick = onClockWidgetClick,
            onLunarPhaseWidgetClick = onLunarPhaseWidgetClick,
            onQuotesWidgetClick = onQuotesWidgetClick
        )

        SetAsDefaultLauncher(
            isDefaultLauncher = settingsState.isDefaultLauncher,
            refreshIsDefaultLauncher = onRefreshIsDefaultLauncher
        )

        About { navigateTo(Screen.About) }

        VerticalSpacer(spacing = 12.dp)
    }
}
