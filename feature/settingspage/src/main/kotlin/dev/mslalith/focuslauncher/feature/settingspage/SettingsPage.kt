package dev.mslalith.focuslauncher.feature.settingspage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.overlay.LocalOverlayHost
import com.slack.circuit.runtime.screen.Screen
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.circuitoverlay.showBottomSheet
import dev.mslalith.focuslauncher.core.screens.AboutScreen
import dev.mslalith.focuslauncher.core.screens.AppDrawerSettingsBottomSheetScreen
import dev.mslalith.focuslauncher.core.screens.ClockWidgetSettingsBottomSheetScreen
import dev.mslalith.focuslauncher.core.screens.EditFavoritesScreen
import dev.mslalith.focuslauncher.core.screens.HideAppsScreen
import dev.mslalith.focuslauncher.core.screens.IconPackScreen
import dev.mslalith.focuslauncher.core.screens.LunarPhaseWidgetSettingsBottomSheetScreen
import dev.mslalith.focuslauncher.core.screens.QuoteWidgetSettingsBottomSheetScreen
import dev.mslalith.focuslauncher.core.screens.SettingsPageScreen
import dev.mslalith.focuslauncher.core.screens.ThemeSelectionBottomSheetScreen
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
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
import kotlinx.coroutines.launch

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
    val scope = rememberCoroutineScope()
    val overlayHost = LocalOverlayHost.current

    fun showBottomSheet(screen: Screen) {
        scope.launch { overlayHost.showBottomSheet(screen) }
    }

    SettingsPage(
        modifier = modifier,
        state = state,
        onThemeClick = { showBottomSheet(screen = ThemeSelectionBottomSheetScreen) },
        onToggleStatusBarVisibility = { eventSink(SettingsPageUiEvent.ToggleStatusBarVisibility) },
        onToggleNotificationShade = { eventSink(SettingsPageUiEvent.ToggleNotificationShade) },
        onAppDrawerClick = { showBottomSheet(screen = AppDrawerSettingsBottomSheetScreen) },
        onClockWidgetClick = { showBottomSheet(screen = ClockWidgetSettingsBottomSheetScreen) },
        onLunarPhaseWidgetClick = { showBottomSheet(screen = LunarPhaseWidgetSettingsBottomSheetScreen) },
        onQuotesWidgetClick = { showBottomSheet(screen = QuoteWidgetSettingsBottomSheetScreen) },
        onRefreshIsDefaultLauncher = { eventSink(SettingsPageUiEvent.RefreshIsDefaultLauncher(context = context)) },
        navigateTo = { eventSink(SettingsPageUiEvent.GoTo(screen = it)) }
    )
}

@Composable
private fun SettingsPage(
    state: SettingsPageState,
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

        EditFavorites { navigateTo(EditFavoritesScreen) }
        HideApps { navigateTo(HideAppsScreen) }

        ToggleStatusBar(
            showStatusBar = state.showStatusBar,
            onClick = onToggleStatusBarVisibility
        )
        PullDownNotifications(
            enableNotificationShade = state.canDrawNotificationShade,
            onClick = onToggleNotificationShade
        )

        IconPack(
            shouldShow = state.showIconPack,
            onClick = { navigateTo(IconPackScreen) }
        )

        AppDrawer(onClick = onAppDrawerClick)

        Widgets(
            onClockWidgetClick = onClockWidgetClick,
            onLunarPhaseWidgetClick = onLunarPhaseWidgetClick,
            onQuotesWidgetClick = onQuotesWidgetClick
        )

        SetAsDefaultLauncher(
            isDefaultLauncher = state.isDefaultLauncher,
            refreshIsDefaultLauncher = onRefreshIsDefaultLauncher
        )

        About { navigateTo(AboutScreen) }

        VerticalSpacer(spacing = 12.dp)
    }
}
