package dev.mslalith.focuslauncher.screens.editfavorites

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.screens.EditFavoritesScreen
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.testSemantics
import dev.mslalith.focuslauncher.core.ui.AppBarWithBackIcon
import dev.mslalith.focuslauncher.core.ui.StatusBarColor
import dev.mslalith.focuslauncher.screens.editfavorites.ui.FavoritesList
import dev.mslalith.focuslauncher.screens.editfavorites.ui.HiddenAppActionText
import dev.mslalith.focuslauncher.screens.editfavorites.utils.TestTags
import kotlinx.coroutines.launch

@CircuitInject(EditFavoritesScreen::class, SingletonComponent::class)
@Composable
fun EditFavorites(
    state: EditFavoritesState,
    modifier: Modifier = Modifier
) {
    // Need to extract the eventSink out to a local val, so that the Compose Compiler
    // treats it as stable. See: https://issuetracker.google.com/issues/256100927
    val eventSink = state.eventSink

    EditFavoritesScreen(
        modifier = modifier,
        state = state,
        onToggleHiddenApps = { eventSink(EditFavoritesUiEvent.ToggleShowingHiddenApps) },
        onClearFavorites = { eventSink(EditFavoritesUiEvent.ClearFavorites) },
        onAddToFavorites = { eventSink(EditFavoritesUiEvent.AddToFavorites(app = it)) },
        onRemoveFromFavorites = { eventSink(EditFavoritesUiEvent.RemoveFromFavorites(app = it)) },
        goBack = { eventSink(EditFavoritesUiEvent.GoBack) }
    )
}

@Composable
private fun EditFavoritesScreen(
    state: EditFavoritesState,
    onToggleHiddenApps: (Boolean) -> Unit,
    onClearFavorites: () -> Unit,
    onAddToFavorites: (App) -> Unit,
    onRemoveFromFavorites: (App) -> Unit,
    goBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    StatusBarColor()

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            AppBarWithBackIcon(
                title = stringResource(id = R.string.favorites),
                onBackPressed = goBack,
                actions = @Composable {
                    HiddenAppActionText(
                        showHiddenApps = state.showHiddenApps,
                        onToggleHiddenApps = onToggleHiddenApps
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onClearFavorites,
                modifier = Modifier.testSemantics(tag = TestTags.TAG_CLEAR_FAVORITES_FAB)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_broom),
                    contentDescription = stringResource(id = R.string.clear_favorites)
                )
            }
        }
    ) { paddingValues ->
        FavoritesList(
            contentPadding = paddingValues,
            favorites = state.favoriteApps,
            showSnackbar = {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(message = it)
                }
            },
            onAddToFavorites = onAddToFavorites,
            onRemoveFromFavorites = onRemoveFromFavorites
        )
    }
}
