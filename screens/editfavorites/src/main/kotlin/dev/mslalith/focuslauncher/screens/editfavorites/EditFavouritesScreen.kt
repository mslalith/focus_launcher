package dev.mslalith.focuslauncher.screens.editfavorites

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.testSemantics
import dev.mslalith.focuslauncher.core.ui.AppBarWithBackIcon
import dev.mslalith.focuslauncher.screens.editfavorites.ui.FavoritesList
import dev.mslalith.focuslauncher.screens.editfavorites.ui.HiddenAppActionText
import dev.mslalith.focuslauncher.screens.editfavorites.utils.TestTags
import kotlinx.coroutines.launch

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
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            AppBarWithBackIcon(
                title = "Favorites",
                onBackPressed = goBack,
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
                modifier = Modifier.testSemantics(tag = TestTags.TAG_CLEAR_FAVORITES_FAB)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_broom),
                    contentDescription = "Clear Favorites"
                )
            }
        }
    ) { paddingValues ->
        val favorites by editFavoritesViewModel.favoritesStateFlow.collectAsStateWithLifecycle()

        FavoritesList(
            contentPadding = paddingValues,
            favorites = favorites,
            showSnackbar = {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(message = it)
                }
            },
            onAddToFavorites = editFavoritesViewModel::addToFavorites,
            onRemoveFromFavorites = editFavoritesViewModel::removeFromFavorites
        )
    }
}
