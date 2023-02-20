package dev.mslalith.focuslauncher.feature.appdrawerpage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import dev.mslalith.focuslauncher.core.common.extensions.launchApp
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.core.ui.SearchField
import dev.mslalith.focuslauncher.core.ui.model.AppWithIcon
import dev.mslalith.focuslauncher.core.ui.providers.LocalLauncherViewManager
import dev.mslalith.focuslauncher.feature.appdrawerpage.apps.grid.AppsGrid
import dev.mslalith.focuslauncher.feature.appdrawerpage.apps.list.AppsList
import dev.mslalith.focuslauncher.feature.appdrawerpage.model.Position
import dev.mslalith.focuslauncher.feature.appdrawerpage.moreoptions.MoreOptionsBottomSheet

@Composable
fun AppDrawerPage(
) {
    AppDrawerPage(appDrawerPageViewModel = hiltViewModel())
}

@Composable
internal fun AppDrawerPage(
    appDrawerPageViewModel: AppDrawerPageViewModel,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val viewManager = LocalLauncherViewManager.current

    val appDrawerPageState by appDrawerPageViewModel.appDrawerPageState.collectAsState()

    var updateAppDisplayAppDialog by remember { mutableStateOf<App?>(null) }

    fun onAppClick(app: AppWithIcon) {
        focusManager.clearFocus()
        context.launchApp(app.toApp())
    }

    fun showMoreOptions(app: AppWithIcon) {
        focusManager.clearFocus()
        viewManager.showBottomSheet {
            MoreOptionsBottomSheet(
                appWithIcon = app,
                isFavorite = { appDrawerPageViewModel.isFavorite(it.packageName) },
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
            onUpdateDisplayName = { appDrawerPageViewModel.updateDisplayName(updatedApp, it) },
            onClose = { updateAppDisplayAppDialog = null }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Box(
            modifier = Modifier.weight(weight = 1f)
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

            ListFadeOutEdgeGradient(position = Position.TOP)
            ListFadeOutEdgeGradient(position = Position.BOTTOM)
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
