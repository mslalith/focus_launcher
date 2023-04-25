package dev.mslalith.focuslauncher.feature.appdrawerpage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.common.extensions.launchApp
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.core.ui.SearchField
import dev.mslalith.focuslauncher.core.ui.model.AppWithIcon
import dev.mslalith.focuslauncher.core.ui.modifiers.verticalFadeOutEdge
import dev.mslalith.focuslauncher.core.ui.providers.LocalLauncherViewManager
import dev.mslalith.focuslauncher.feature.appdrawerpage.apps.grid.AppsGrid
import dev.mslalith.focuslauncher.feature.appdrawerpage.apps.list.AppsList
import dev.mslalith.focuslauncher.feature.appdrawerpage.moreoptions.MoreOptionsBottomSheet

@Composable
fun AppDrawerPage(
) {
    AppDrawerPage(appDrawerPageViewModel = hiltViewModel())
}

@Composable
internal fun AppDrawerPage(
    appDrawerPageViewModel: AppDrawerPageViewModel
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val viewManager = LocalLauncherViewManager.current

    val appDrawerPageState by appDrawerPageViewModel.appDrawerPageState.collectAsStateWithLifecycle()

    var updateAppDisplayAppDialog by remember { mutableStateOf<App?>(value = null) }

    fun onAppClick(app: AppWithIcon) {
        focusManager.clearFocus()
        context.launchApp(app = app.toApp())
    }

    fun showMoreOptions(app: AppWithIcon) {
        focusManager.clearFocus()
        viewManager.showBottomSheet {
            MoreOptionsBottomSheet(
                appWithIcon = app,
                isFavorite = { appDrawerPageViewModel.isFavorite(packageName = it.packageName) },
                addToFavorites = appDrawerPageViewModel::addToFavorites,
                removeFromFavorites = appDrawerPageViewModel::removeFromFavorites,
                addToHiddenApps = appDrawerPageViewModel::addToHiddenApps,
                onUpdateDisplayNameClick = { updateAppDisplayAppDialog = app.toApp() },
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
            when (appDrawerPageState.appDrawerViewType) {
                AppDrawerViewType.LIST -> AppsList(
                    apps = appDrawerPageState.allApps,
                    showAppIcons = appDrawerPageState.showAppIcons,
                    showAppGroupHeader = appDrawerPageState.showAppGroupHeader,
                    isSearchQueryEmpty = appDrawerPageState.searchBarQuery.isEmpty(),
                    onAppClick = ::onAppClick,
                    onAppLongClick = ::showMoreOptions
                )

                AppDrawerViewType.GRID -> AppsGrid(
                    apps = appDrawerPageState.allApps,
                    onAppClick = ::onAppClick,
                    onAppLongClick = ::showMoreOptions
                )
            }
        }

        AnimatedVisibility(visible = appDrawerPageState.showSearchBar) {
            SearchField(
                placeholder = "Search app...",
                query = appDrawerPageState.searchBarQuery,
                onQueryChange = appDrawerPageViewModel::searchAppQuery
            )
        }
    }
}
