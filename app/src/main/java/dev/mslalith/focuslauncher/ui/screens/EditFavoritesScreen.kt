package dev.mslalith.focuslauncher.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mslalith.focuslauncher.R
import dev.mslalith.focuslauncher.core.model.SelectedApp
import dev.mslalith.focuslauncher.core.ui.AppBarWithBackIcon
import dev.mslalith.focuslauncher.core.ui.ExtendedMiniFab
import dev.mslalith.focuslauncher.core.ui.SelectableCheckboxItem
import dev.mslalith.focuslauncher.core.ui.extensions.showSnackbar
import dev.mslalith.focuslauncher.core.ui.providers.LocalNavController
import dev.mslalith.focuslauncher.ui.viewmodels.AppsViewModel
import kotlinx.coroutines.launch

@Composable
fun EditFavoritesScreen(
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
                title = "Favorites",
                onBackPressed = { goBack() },
                actions = { HiddenAppActionText(appsViewModel) }
            )
        },
        floatingActionButton = {
            ExtendedMiniFab(
                text = "Clear Favorites",
                icon = Icons.Rounded.Refresh,
                onClick = { appsViewModel.clearFavorites() }
            )
        }
    ) {
        FavoritesList(
            modifier = Modifier.padding(it),
            scaffoldState = scaffoldState,
            appsViewModel = appsViewModel
        )
    }
}

@Composable
private fun HiddenAppActionText(
    appsViewModel: AppsViewModel
) {
    val showHiddenApps by appsViewModel.showHiddenAppsInFavorites.collectAsState()

    val textDecoration = when (showHiddenApps) {
        true -> TextDecoration.None
        false -> TextDecoration.LineThrough
    }

    TextButton(
        onClick = { appsViewModel.shouldShowHiddenAppsInFavorites(!showHiddenApps) },
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colors.onBackground
        ),
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Text(
            text = " Hidden Apps ",
            style = TextStyle(
                fontSize = 12.sp,
                textDecoration = textDecoration
            )
        )
    }
}

@Composable
private fun FavoritesList(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
    appsViewModel: AppsViewModel
) {
    val coroutineScope = rememberCoroutineScope()

    val favoritesList by appsViewModel.favoritesStateFlow.collectAsState()

    val appHiddenMessage = stringResource(R.string.app_hidden_message)

    fun toggleFavorite(selectedApp: SelectedApp, isHidden: Boolean) {
        if (!isHidden && selectedApp.isSelected) {
            appsViewModel.apply {
                if (selectedApp.isSelected) {
                    removeFromFavorites(selectedApp.app)
                } else {
                    addToFavorites(selectedApp.app)
                }
            }
            return
        }

        if (isHidden) {
            coroutineScope.launch {
                scaffoldState.showSnackbar(
                    message = appHiddenMessage.replace("{}", selectedApp.app.name),
                    discardIfShowing = true
                )
            }
        }
    }

    LazyColumn(modifier = modifier) {
        items(
            items = favoritesList
        ) { favorite ->
            FavoriteListItem(
                selectedApp = favorite,
                onAppClick = { toggleFavorite(favorite, favorite.disabled) }
            )
        }
        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
private fun FavoriteListItem(
    selectedApp: SelectedApp,
    onAppClick: () -> Unit
) {
    SelectableCheckboxItem(
        text = selectedApp.app.name,
        checked = selectedApp.isSelected,
        disabled = selectedApp.disabled,
        onClick = { onAppClick() }
    )
}
