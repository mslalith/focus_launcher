package dev.mslalith.focuslauncher.feature.appdrawerpage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.overlay.LocalOverlayHost
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.circuitoverlay.bottomsheet.showBottomSheet
import dev.mslalith.focuslauncher.core.circuitoverlay.bottomsheet.showBottomSheetWithResult
import dev.mslalith.focuslauncher.core.common.extensions.launchApp
import dev.mslalith.focuslauncher.core.common.model.LoadingState
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerItem
import dev.mslalith.focuslauncher.core.screens.AppDrawerPageScreen
import dev.mslalith.focuslauncher.core.screens.AppMoreOptionsBottomSheetScreen
import dev.mslalith.focuslauncher.core.screens.BottomSheetScreen
import dev.mslalith.focuslauncher.core.screens.UpdateAppDisplayNameBottomSheetScreen
import dev.mslalith.focuslauncher.core.ui.DotWaveLoader
import dev.mslalith.focuslauncher.core.ui.SearchField
import dev.mslalith.focuslauncher.core.ui.effects.OnDayChangeListener
import dev.mslalith.focuslauncher.core.ui.modifiers.verticalFadeOutEdge
import dev.mslalith.focuslauncher.core.ui.providers.LocalLauncherPagerState
import dev.mslalith.focuslauncher.feature.appdrawerpage.apps.grid.AppsGrid
import dev.mslalith.focuslauncher.feature.appdrawerpage.apps.list.AppsList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@CircuitInject(AppDrawerPageScreen::class, SingletonComponent::class)
@Composable
fun AppDrawerPage(
    state: AppDrawerPageState,
    modifier: Modifier = Modifier
) {
    // Need to extract the eventSink out to a local val, so that the Compose Compiler
    // treats it as stable. See: https://issuetracker.google.com/issues/256100927
    val eventSink = state.eventSink

    val scope = rememberCoroutineScope()
    val overlayHost = LocalOverlayHost.current

    fun showBottomSheet(screen: BottomSheetScreen<Unit>) {
        scope.launch { overlayHost.showBottomSheet(screen) }
    }

    fun showAppMoreOptionsBottomSheetScreen(appDrawerItem: AppDrawerItem) {
        scope.launch {
            when (overlayHost.showBottomSheetWithResult(AppMoreOptionsBottomSheetScreen(appDrawerItem = appDrawerItem))) {
                is AppMoreOptionsBottomSheetScreen.Result.ShowUpdateAppDisplayBottomSheet -> showBottomSheet(screen = UpdateAppDisplayNameBottomSheetScreen(app = appDrawerItem.app))
                null -> Unit
            }
        }
    }

    AppDrawerPageKeyboardAware(
        modifier = modifier,
        state = state,
        onSearchQueryChange = { eventSink(AppDrawerPageUiEvent.UpdateSearchQuery(query = it)) },
        reloadIconPack = { eventSink(AppDrawerPageUiEvent.ReloadIconPack) },
        showAppMoreOptions = ::showAppMoreOptionsBottomSheetScreen
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AppDrawerPageKeyboardAware(
    state: AppDrawerPageState,
    onSearchQueryChange: (String) -> Unit,
    reloadIconPack: () -> Unit,
    showAppMoreOptions: (AppDrawerItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val pagerState = LocalLauncherPagerState.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }.collectLatest { page ->
            if (page != 2) {
                onSearchQueryChange("")
                keyboardController?.hide()
            }
        }
    }

    fun onAppClick(appDrawerItem: AppDrawerItem) {
        focusManager.clearFocus()
        context.launchApp(app = appDrawerItem.app)
        onSearchQueryChange("")
    }

    fun onAppLongClick(appDrawerItem: AppDrawerItem) {
        focusManager.clearFocus()
        showAppMoreOptions(appDrawerItem)
    }

    AppDrawerPageInternal(
        modifier = modifier,
        appDrawerPageState = state,
        onSearchQueryChange = onSearchQueryChange,
        onAppClick = ::onAppClick,
        onAppLongClick = ::onAppLongClick,
        reloadIconPack = reloadIconPack
    )
}

@Composable
internal fun AppDrawerPageInternal(
    appDrawerPageState: AppDrawerPageState,
    onSearchQueryChange: (String) -> Unit,
    onAppClick: (AppDrawerItem) -> Unit,
    onAppLongClick: (AppDrawerItem) -> Unit,
    reloadIconPack: () -> Unit,
    modifier: Modifier = Modifier
) {
    OnDayChangeListener {
        reloadIconPack()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Box(
            modifier = Modifier
                .weight(weight = 1f)
                .verticalFadeOutEdge(
                    height = 16.dp,
                    color = MaterialTheme.colorScheme.surface
                )
        ) {
            when (val allAppsState = appDrawerPageState.allAppsState) {
                is LoadingState.Loaded -> {
                    when (appDrawerPageState.appDrawerViewType) {
                        AppDrawerViewType.LIST -> AppsList(
                            apps = allAppsState.value,
                            appDrawerIconViewType = appDrawerPageState.appDrawerIconViewType,
                            showAppGroupHeader = appDrawerPageState.showAppGroupHeader,
                            isSearchQueryEmpty = appDrawerPageState.searchBarQuery.isEmpty(),
                            onAppClick = onAppClick,
                            onAppLongClick = onAppLongClick
                        )

                        AppDrawerViewType.GRID -> AppsGrid(
                            apps = allAppsState.value,
                            appDrawerIconViewType = appDrawerPageState.appDrawerIconViewType,
                            onAppClick = onAppClick,
                            onAppLongClick = onAppLongClick
                        )
                    }
                }

                LoadingState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        DotWaveLoader()
                    }
                }
            }
        }

        AnimatedVisibility(visible = appDrawerPageState.showSearchBar) {
            SearchField(
                placeholder = stringResource(id = R.string.search_app_hint),
                query = appDrawerPageState.searchBarQuery,
                onQueryChange = onSearchQueryChange
            )
        }
    }
}
