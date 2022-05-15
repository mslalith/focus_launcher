package dev.mslalith.focuslauncher.ui.screens.pages

import android.app.Activity
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import dev.mslalith.focuslauncher.data.models.AppDrawerSettingsProperties
import dev.mslalith.focuslauncher.data.models.BottomSheetContentType
import dev.mslalith.focuslauncher.data.models.ClockSettingsProperties
import dev.mslalith.focuslauncher.data.models.LunarPhaseSettingsProperties
import dev.mslalith.focuslauncher.data.models.QuotesSettingsProperties
import dev.mslalith.focuslauncher.data.models.Screen
import dev.mslalith.focuslauncher.data.models.WidgetType
import dev.mslalith.focuslauncher.data.providers.LocalLauncherViewManager
import dev.mslalith.focuslauncher.data.providers.LocalNavController
import dev.mslalith.focuslauncher.data.providers.LocalSystemUiController
import dev.mslalith.focuslauncher.data.providers.LocalUpdateManager
import dev.mslalith.focuslauncher.data.utils.AppUpdateState.CheckForUpdates
import dev.mslalith.focuslauncher.data.utils.AppUpdateState.CheckingForUpdates
import dev.mslalith.focuslauncher.data.utils.AppUpdateState.Downloaded
import dev.mslalith.focuslauncher.data.utils.AppUpdateState.Downloading
import dev.mslalith.focuslauncher.data.utils.AppUpdateState.Installing
import dev.mslalith.focuslauncher.data.utils.AppUpdateState.NoUpdateAvailable
import dev.mslalith.focuslauncher.data.utils.AppUpdateState.TryAgain
import dev.mslalith.focuslauncher.extensions.VerticalSpacer
import dev.mslalith.focuslauncher.ui.viewmodels.AppsViewModel
import dev.mslalith.focuslauncher.ui.viewmodels.SettingsViewModel
import dev.mslalith.focuslauncher.ui.viewmodels.ThemeViewModel
import dev.mslalith.focuslauncher.ui.viewmodels.WidgetsViewModel
import dev.mslalith.focuslauncher.ui.views.onLifecycleEventChange
import dev.mslalith.focuslauncher.ui.views.settings.LoadingSettingsItem
import dev.mslalith.focuslauncher.ui.views.settings.SettingsExpandableItem
import dev.mslalith.focuslauncher.ui.views.settings.SettingsGridContent
import dev.mslalith.focuslauncher.ui.views.settings.SettingsGridItem
import dev.mslalith.focuslauncher.ui.views.settings.SettingsItem
import kotlinx.coroutines.launch

@Composable
fun SettingsPage(
    themeViewModel: ThemeViewModel,
    appsViewModel: AppsViewModel,
    settingsViewModel: SettingsViewModel,
    widgetsViewModel: WidgetsViewModel,
) {
    val navController = LocalNavController.current
    val scrollState = rememberScrollState()

    fun navigateTo(screen: Screen) = navController.navigate(screen.id)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
    ) {
        SettingsHeader()
        VerticalSpacer(spacing = 12.dp)

        ChangeTheme(themeViewModel)
        EditFavorites(::navigateTo)
        HideApps(::navigateTo)
        ToggleStatusBar(settingsViewModel)
        PullDownNotifications(settingsViewModel)
        AppDrawer(appsViewModel, settingsViewModel)
        Widgets(widgetsViewModel, settingsViewModel)
        SetAsDefaultLauncher(settingsViewModel)
        // CheckForUpdates()

        VerticalSpacer(spacing = 12.dp)
    }
}

@Composable
private fun SettingsHeader() {
    // this should be relative to ITEM_PADDING from SettingsItem.kt
    val usableHorizontalPadding = 24.dp.times(other = 1.3f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = usableHorizontalPadding),
    ) {
        Text(
            text = "Settings",
            style = TextStyle(
                color = MaterialTheme.colors.onBackground,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
        )

        Divider(
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.fillMaxWidth(fraction = 0.5f),
        )
    }
}

@Composable
private fun ChangeTheme(themeViewModel: ThemeViewModel) {
    val currentTheme by themeViewModel.currentThemeStateFlow.collectAsState()

    SettingsExpandableItem(text = "Change Theme") { closeExpandable ->
        SettingsGridContent(items = ThemeViewModel.allThemes) { theme ->
            val isAppliedTheme = currentTheme == theme
            SettingsGridItem(
                text = theme.text,
                showIcon = true,
                icon = if (isAppliedTheme) Icons.Rounded.Done else null,
                contentDescription = if (isAppliedTheme) "Selected Theme" else theme.text,
                onClick = {
                    if (!isAppliedTheme) themeViewModel.changeTheme(theme)
                    closeExpandable()
                }
            )
        }
    }
}

@Composable
private fun EditFavorites(navigateTo: (Screen) -> Unit) {
    SettingsItem(text = "Edit Favorites") { navigateTo(Screen.EditFavorites) }
}

@Composable
private fun HideApps(navigateTo: (Screen) -> Unit) {
    SettingsItem(text = "Hide Apps") { navigateTo(Screen.HideApps) }
}

@Composable
private fun ToggleStatusBar(settingsViewModel: SettingsViewModel) {
    val systemUiController = LocalSystemUiController.current
    val showStatusBar by settingsViewModel.statusBarVisibilityStateFlow.collectAsState()
    val showOrHideText = when (showStatusBar) {
        true -> {
            systemUiController.isStatusBarVisible = true
            "Hide"
        }
        false -> {
            systemUiController.isStatusBarVisible = false
            "Show"
        }
    }

    SettingsItem(text = "$showOrHideText Status Bar") {
        settingsViewModel.toggleStatusBarVisibility()
    }
}

@Composable
private fun PullDownNotifications(settingsViewModel: SettingsViewModel) {
    val enableNotificationShade by settingsViewModel.notificationShadeStateFlow.collectAsState()
    val enableOrDisableText = if (enableNotificationShade) "Disable" else "Enable"
    SettingsItem(text = "$enableOrDisableText Pull down Notifications") {
        settingsViewModel.toggleNotificationShade()
    }
}

@Composable
private fun AppDrawer(
    appsViewModel: AppsViewModel,
    settingsViewModel: SettingsViewModel,
) {
    val viewManager = LocalLauncherViewManager.current
    SettingsItem(text = "App Drawer") {
        viewManager.showBottomSheet(
            sheetType = BottomSheetContentType.AppDrawer(
                properties = AppDrawerSettingsProperties(
                    appsViewModel = appsViewModel,
                    settingsViewModel = settingsViewModel,
                ),
            ),
        )
    }
}

@Composable
private fun Widgets(
    widgetsViewModel: WidgetsViewModel,
    settingsViewModel: SettingsViewModel,
) {
    val viewManager = LocalLauncherViewManager.current

    fun onWidgetTypeClick(action: () -> BottomSheetContentType) {
        viewManager.showBottomSheet(action())
    }

    SettingsExpandableItem(text = "Widgets") {
        SettingsGridContent(items = WidgetType.values().toList()) { widgetType ->
            SettingsGridItem(
                text = widgetType.text,
                onClick = {
                    when (widgetType) {
                        WidgetType.CLOCK -> onWidgetTypeClick {
                            BottomSheetContentType.Widgets.Clock(
                                properties = ClockSettingsProperties(
                                    widgetsViewModel = widgetsViewModel,
                                    settingsViewModel = settingsViewModel,
                                )
                            )
                        }
                        WidgetType.LUNAR_PHASE -> onWidgetTypeClick {
                            BottomSheetContentType.Widgets.LunarPhase(
                                properties = LunarPhaseSettingsProperties(
                                    widgetsViewModel = widgetsViewModel,
                                    settingsViewModel = settingsViewModel,
                                ),
                            )
                        }
                        WidgetType.QUOTES -> onWidgetTypeClick {
                            BottomSheetContentType.Widgets.Quotes(
                                properties = QuotesSettingsProperties(
                                    widgetsViewModel = widgetsViewModel,
                                    settingsViewModel = settingsViewModel,
                                ),
                            )
                        }
                    }
                },
            )
        }
    }
}

@Composable
private fun SetAsDefaultLauncher(
    settingsViewModel: SettingsViewModel
) {
    val context = LocalContext.current
    val isDefaultLauncher by settingsViewModel.isDefaultLauncherStateFlow.collectAsState()

    val launchForHome = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        settingsViewModel.refreshIsDefaultLauncher(context)
    }

    fun askToSetAsDefaultLauncher() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = context.getSystemService(Context.ROLE_SERVICE) as RoleManager
            val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_HOME)
            launchForHome.launch(intent)
        } else {
            context.startActivity(Intent(Settings.ACTION_HOME_SETTINGS))
        }
    }

    onLifecycleEventChange { event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            settingsViewModel.refreshIsDefaultLauncher(context)
        }
    }

    AnimatedVisibility(
        visible = !isDefaultLauncher,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        SettingsItem(text = "Set as Default Launcher") {
            askToSetAsDefaultLauncher()
        }
    }
}

@Composable
private fun CheckForUpdates() {
    val context = LocalContext.current
    val viewManager = LocalLauncherViewManager.current
    val updateManager = LocalUpdateManager.current
    val coroutineScope = rememberCoroutineScope()
    val appUpdateState by updateManager.appUpdateStateFlow.collectAsState()

    LaunchedEffect(key1 = appUpdateState) {
        if (appUpdateState is TryAgain || appUpdateState is NoUpdateAvailable) {
            coroutineScope.launch { viewManager.showSnackbar(message = appUpdateState.message) }
            updateManager.resetAppUpdateState()
        }
    }

    Crossfade(targetState = appUpdateState) { state ->
        val onClick: (() -> Unit)? = when (state) {
            is CheckForUpdates -> {
                { updateManager.checkForUpdate(context as Activity) }
            }
            Downloaded -> {
                { updateManager.completeUpdate() }
            }
            CheckingForUpdates, Installing, is Downloading -> null
            else -> {
                {
                    updateManager.resetAppUpdateState()
                    coroutineScope.launch { viewManager.showSnackbar(message = state.message) }
                }
            }
        }

        LoadingSettingsItem(
            text = state.message,
            isLoading = state is CheckingForUpdates,
            onClick = onClick,
        )
    }
}
