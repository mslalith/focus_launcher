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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.common.LoadingState
import dev.mslalith.focuslauncher.core.common.extensions.launchApp
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.core.model.app.AppWithIconFavorite
import dev.mslalith.focuslauncher.core.ui.DotWaveLoader
import dev.mslalith.focuslauncher.core.ui.SearchField
import dev.mslalith.focuslauncher.core.ui.effects.OnDayChangeListener
import dev.mslalith.focuslauncher.core.ui.modifiers.verticalFadeOutEdge
import dev.mslalith.focuslauncher.core.ui.providers.LocalLauncherPagerState
import dev.mslalith.focuslauncher.core.ui.providers.LocalLauncherViewManager
import dev.mslalith.focuslauncher.feature.appdrawerpage.apps.grid.AppsGrid
import dev.mslalith.focuslauncher.feature.appdrawerpage.apps.list.AppsList
import dev.mslalith.focuslauncher.feature.appdrawerpage.moreoptions.MoreOptionsBottomSheet
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AppDrawerPage() {
    AppDrawerPageKeyboardAware()
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
internal fun AppDrawerPageKeyboardAware() {
    val keyboardController = LocalSoftwareKeyboardController.current
    val pagerState = LocalLauncherPagerState.current

    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }.collectLatest { page ->
            if (page != 2) keyboardController?.hide()
        }
    }

    AppDrawerPageInternal()
}

@Composable
internal fun AppDrawerPageInternal(
    appDrawerPageViewModel: AppDrawerPageViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val viewManager = LocalLauncherViewManager.current

    val appDrawerPageState by appDrawerPageViewModel.appDrawerPageState.collectAsStateWithLifecycle()

    var updateAppDisplayAppDialog by remember { mutableStateOf<App?>(value = null) }

    fun onAppClick(appWithIconFavorite: AppWithIconFavorite) {
        focusManager.clearFocus()
        context.launchApp(app = appWithIconFavorite.appWithIcon.app)
    }

    fun showMoreOptions(appWithIconFavorite: AppWithIconFavorite) {
        focusManager.clearFocus()
        viewManager.showBottomSheet {
            MoreOptionsBottomSheet(
                appWithIconFavorite = appWithIconFavorite,
                addToFavorites = appDrawerPageViewModel::addToFavorites,
                removeFromFavorites = appDrawerPageViewModel::removeFromFavorites,
                addToHiddenApps = appDrawerPageViewModel::addToHiddenApps,
                onUpdateDisplayNameClick = { updateAppDisplayAppDialog = appWithIconFavorite.appWithIcon.app },
                onClose = { viewManager.hideBottomSheet() }
            )
        }
    }

    updateAppDisplayAppDialog?.let { updatedApp ->
        UpdateAppDisplayNameDialog(
            app = updatedApp,
            onUpdateDisplayName = { appDrawerPageViewModel.updateDisplayName(app = updatedApp, displayName = it) },
            onClose = { updateAppDisplayAppDialog = null }
        )
    }

    OnDayChangeListener {
        appDrawerPageViewModel.reloadIconPack()
    }

    Column(
        modifier = Modifier
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
                            showAppIcons = appDrawerPageState.showAppIcons,
                            showAppGroupHeader = appDrawerPageState.showAppGroupHeader,
                            isSearchQueryEmpty = appDrawerPageState.searchBarQuery.isEmpty(),
                            onAppClick = ::onAppClick,
                            onAppLongClick = ::showMoreOptions
                        )

                        AppDrawerViewType.GRID -> AppsGrid(
                            apps = allAppsState.value,
                            onAppClick = ::onAppClick,
                            onAppLongClick = ::showMoreOptions
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
                onQueryChange = appDrawerPageViewModel::searchAppQuery
            )
        }
    }
}
