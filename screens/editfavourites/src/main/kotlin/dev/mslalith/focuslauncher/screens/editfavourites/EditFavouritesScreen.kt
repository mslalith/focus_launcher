package dev.mslalith.focuslauncher.screens.editfavourites

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
import dev.mslalith.focuslauncher.screens.editfavourites.ui.FavoritesList
import dev.mslalith.focuslauncher.screens.editfavourites.ui.HiddenAppActionText
import dev.mslalith.focuslauncher.screens.editfavourites.utils.TestTags

@Composable
fun EditFavouritesScreen(
    goBack: () -> Unit
) {
    EditFavouritesScreen(
        editFavouritesViewModel = hiltViewModel(),
        goBack = goBack
    )
}

@Composable
internal fun EditFavouritesScreen(
    editFavouritesViewModel: EditFavouritesViewModel,
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
                    val showHiddenApps by editFavouritesViewModel.showHiddenAppsInFavorites.collectAsState()

                    HiddenAppActionText(
                        showHiddenApps = showHiddenApps,
                        onToggleHiddenApps = editFavouritesViewModel::shouldShowHiddenAppsInFavorites
                    )
                }
            )
        },
        floatingActionButton = {
            ExtendedMiniFab(
                modifier = Modifier.testSemantics(tag = TestTags.TAG_CLEAR_FAVOURITES_FAB),
                text = "Clear Favorites",
                icon = Icons.Rounded.Refresh,
                onClick = editFavouritesViewModel::clearFavourites
            )
        }
    ) { paddingValues ->
        val favorites by editFavouritesViewModel.favouritesStateFlow.collectAsState()

        FavoritesList(
            modifier = Modifier.padding(paddingValues),
            scaffoldState = scaffoldState,
            favorites = favorites,
            onAddToFavorites = { editFavouritesViewModel.addToFavourites(it) },
            onRemoveFromFavorites = { editFavouritesViewModel.removeFromFavorites(it) }
        )
    }
}
