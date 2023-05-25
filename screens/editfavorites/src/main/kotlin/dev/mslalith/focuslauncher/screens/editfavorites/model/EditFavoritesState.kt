package dev.mslalith.focuslauncher.screens.editfavorites.model

import androidx.compose.runtime.Immutable
import dev.mslalith.focuslauncher.core.model.app.SelectedApp
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class EditFavoritesState(
    val favoriteApps: ImmutableList<SelectedApp>,
    val showHiddenApps: Boolean
)
