package dev.mslalith.focuslauncher.screens.editfavorites

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.testSemantics
import dev.mslalith.focuslauncher.core.ui.AppBarWithBackIcon
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
                    val showHiddenApps by editFavoritesViewModel.showHiddenAppsInFavorites.collectAsStateWithLifecycle()

                    HiddenAppActionText(
                        showHiddenApps = showHiddenApps,
                        onToggleHiddenApps = editFavoritesViewModel::shouldShowHiddenAppsInFavorites
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = editFavoritesViewModel::clearFavorites,
                backgroundColor = MaterialTheme.colors.onBackground.copy(alpha = 0.85f),
                modifier = Modifier.testSemantics(tag = TestTags.TAG_CLEAR_FAVORITES_FAB),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_broom),
                    contentDescription = "Clear Favorites",
                    tint = MaterialTheme.colors.background,
                )
            }
        }
    ) { paddingValues ->
        val favorites by editFavoritesViewModel.favoritesStateFlow.collectAsStateWithLifecycle()

        FavoritesList(
            modifier = Modifier.padding(paddingValues),
            scaffoldState = scaffoldState,
            favorites = favorites,
            onAddToFavorites = { editFavoritesViewModel.addToFavorites(it) },
            onRemoveFromFavorites = { editFavoritesViewModel.removeFromFavorites(it) }
        )
    }
}
