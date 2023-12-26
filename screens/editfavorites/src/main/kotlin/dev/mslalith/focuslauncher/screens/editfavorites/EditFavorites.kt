package dev.mslalith.focuslauncher.screens.editfavorites

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.screens.EditFavoritesScreen

@CircuitInject(EditFavoritesScreen::class, SingletonComponent::class)
@Composable
fun EditFavorites(
    state: EditFavoritesState,
    modifier: Modifier = Modifier
) {
    // Need to extract the eventSink out to a local val, so that the Compose Compiler
    // treats it as stable. See: https://issuetracker.google.com/issues/256100927
    val eventSink = state.eventSink

    EditFavoritesScreenInternal(
        modifier = modifier,
        editFavoritesState = dev.mslalith.focuslauncher.screens.editfavorites.model.EditFavoritesState(state.favoriteApps, state.showHiddenApps),
        onToggleHiddenApps = { eventSink(EditFavoritesUiEvent.ToggleShowingHiddenApps) },
        onClearFavorites = { eventSink(EditFavoritesUiEvent.ClearFavorites) },
        onAddToFavorites = { eventSink(EditFavoritesUiEvent.AddToFavorites(app = it)) },
        onRemoveFromFavorites = { eventSink(EditFavoritesUiEvent.RemoveFromFavorites(app = it)) },
        goBack = { eventSink(EditFavoritesUiEvent.GoBack) }
    )
}
