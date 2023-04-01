package dev.mslalith.focuslauncher.screens.editfavorites

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.testSemantics
import dev.mslalith.focuslauncher.core.ui.AppBarWithBackIcon
import dev.mslalith.focuslauncher.core.ui.ExtendedMiniFab
import dev.mslalith.focuslauncher.screens.editfavorites.ui.FavoritesList
import dev.mslalith.focuslauncher.screens.editfavorites.ui.HiddenAppActionText
import dev.mslalith.focuslauncher.screens.editfavorites.utils.TestTags

@Composable
fun EditFavoritesScreen(
    goBack: () -> Unit
) {
    EditFavoritesScreen(
        editFavoritesViewModel = hiltViewModel(),
        goBack = goBack
    )
}

@Composable
internal fun EditFavoritesScreen(
    editFavoritesViewModel: EditFavoritesViewModel,
    goBack: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            AppBarWithBackIcon(
                title = "Favorites",
                onBackPressed = { goBack() },
                actions = @Composable {
                    val showHiddenApps by editFavoritesViewModel.showHiddenAppsInFavorites.collectAsState()

                    HiddenAppActionText(
                        showHiddenApps = showHiddenApps,
                        onToggleHiddenApps = editFavoritesViewModel::shouldShowHiddenAppsInFavorites
                    )
                }
            )
        },
        floatingActionButton = {
            ExtendedMiniFab(
                modifier = Modifier.testSemantics(tag = TestTags.TAG_CLEAR_FAVORITES_FAB),
                text = "Clear Favorites",
                icon = Icons.Rounded.Refresh,
                onClick = editFavoritesViewModel::clearFavorites
            )
        }
    ) { paddingValues ->
        val favorites by editFavoritesViewModel.favoritesStateFlow.collectAsState()

        FavoritesList(
            modifier = Modifier.padding(paddingValues),
            scaffoldState = scaffoldState,
            favorites = favorites,
            onAddToFavorites = { editFavoritesViewModel.addToFavorites(it) },
            onRemoveFromFavorites = { editFavoritesViewModel.removeFromFavorites(it) }
        )
    }
}
