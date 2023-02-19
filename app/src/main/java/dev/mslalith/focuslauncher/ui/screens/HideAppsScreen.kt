package dev.mslalith.focuslauncher.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.R
import dev.mslalith.focuslauncher.core.model.SelectedApp
import dev.mslalith.focuslauncher.core.ui.AppBarWithBackIcon
import dev.mslalith.focuslauncher.core.ui.ExtendedMiniFab
import dev.mslalith.focuslauncher.data.providers.LocalNavController
import dev.mslalith.focuslauncher.ui.viewmodels.AppsViewModel
import dev.mslalith.focuslauncher.ui.views.ConfirmSelectableItem
import dev.mslalith.focuslauncher.ui.views.ConfirmSelectableItemType
import dev.mslalith.focuslauncher.ui.views.SelectableCheckboxItem
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun HideAppsScreen(
    appsViewModel: AppsViewModel
) {
    val navController = LocalNavController.current
    val scaffoldState = rememberScaffoldState()

    fun goBack() = navController.popBackStack()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            AppBarWithBackIcon(
                title = "Hide Apps",
                onBackPressed = { goBack() }
            )
        },
        floatingActionButton = {
            ExtendedMiniFab(
                text = "Clear Hidden Apps",
                icon = Icons.Rounded.Refresh,
                onClick = { appsViewModel.clearHiddenApps() }
            )
        }
    ) {
        HiddenAppsList(
            modifier = Modifier.padding(it),
            appsViewModel = appsViewModel
        )
    }
}

@Composable
private fun HiddenAppsList(
    modifier: Modifier = Modifier,
    appsViewModel: AppsViewModel
) {
    val coroutineScope = rememberCoroutineScope()

    val hiddenAppsList by appsViewModel.hiddenAppsStateFlow.collectAsState()

    fun toggleHiddenApp(selectedApp: SelectedApp) {
        appsViewModel.apply {
            if (selectedApp.isSelected) {
                removeFromHiddenApps(selectedApp.app)
            } else {
                coroutineScope.launch { appsViewModel.addToHiddenApps(selectedApp.app) }
                removeFromFavorites(selectedApp.app)
            }
        }
    }

    LazyColumn(modifier = modifier) {
        items(
            items = hiddenAppsList
        ) { hiddenApp ->
            val isFavorite = runBlocking { appsViewModel.isFavorite(hiddenApp.app.packageName) }
            HiddenAppListItem(
                selectedApp = hiddenApp,
                isFavorite = isFavorite,
                onAppClick = { toggleHiddenApp(hiddenApp) }
            )
        }
        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
private fun HiddenAppListItem(
    selectedApp: SelectedApp,
    isFavorite: Boolean,
    onAppClick: () -> Unit
) {
    val appName = selectedApp.app.name
    val confirmToHideMessage = stringResource(R.string.hide_favorite_app_message, appName)

    if (isFavorite) {
        ConfirmSelectableItem(
            text = appName,
            confirmMessage = confirmToHideMessage,
            itemType = ConfirmSelectableItemType.Checkbox(
                checked = selectedApp.isSelected
            ),
            confirmText = "Yes, Hide",
            onConfirm = {
                if (it) onAppClick()
            }
        )
    } else {
        SelectableCheckboxItem(
            text = appName,
            checked = selectedApp.isSelected,
            onClick = onAppClick
        )
    }
}
