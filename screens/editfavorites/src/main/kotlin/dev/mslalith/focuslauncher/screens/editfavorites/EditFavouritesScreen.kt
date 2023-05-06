package dev.mslalith.focuslauncher.screens.editfavorites

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.testSemantics
import dev.mslalith.focuslauncher.core.ui.AppBarWithBackIcon
import dev.mslalith.focuslauncher.core.ui.StatusBarColor
import dev.mslalith.focuslauncher.screens.editfavorites.ui.FavoritesList
import dev.mslalith.focuslauncher.screens.editfavorites.ui.HiddenAppActionText
import dev.mslalith.focuslauncher.screens.editfavorites.utils.TestTags
import kotlinx.coroutines.launch

@Composable
fun EditFavoritesScreen(
    goBack: () -> Unit
) {
    EditFavoritesScreenInternal(
        goBack = goBack
    )
}

@Composable
internal fun EditFavoritesScreenInternal(
    editFavoritesViewModel: EditFavoritesViewModel = hiltViewModel(),
    goBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    StatusBarColor(hasTopAppBar = true)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            AppBarWithBackIcon(
                title = stringResource(id = R.string.favorites),
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
                    contentDescription = stringResource(id = R.string.clear_favorites)
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
