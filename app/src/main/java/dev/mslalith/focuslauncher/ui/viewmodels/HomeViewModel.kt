package dev.mslalith.focuslauncher.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.data.model.App
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

sealed class FavoritesContextMode {
    object Open : FavoritesContextMode()
    object Closed : FavoritesContextMode()
    object Remove : FavoritesContextMode()
    object Reorder : FavoritesContextMode()
    data class ReorderPickPosition(val app: App) : FavoritesContextMode()

    fun isInContextualMode(): Boolean = this != Closed
}

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _favoritesContextualMode: MutableStateFlow<FavoritesContextMode> = MutableStateFlow(value = FavoritesContextMode.Closed)
    val favoritesContextualMode = _favoritesContextualMode.asStateFlow()

    fun isInContextualMode(): Boolean = _favoritesContextualMode.value != FavoritesContextMode.Closed

    fun changeFavoritesContextMode(mode: FavoritesContextMode) {
        _favoritesContextualMode.value = mode
    }

    fun isReordering(): Boolean = when (_favoritesContextualMode.value) {
        FavoritesContextMode.Reorder, is FavoritesContextMode.ReorderPickPosition -> true
        else -> false
    }

    fun isAppAboutToReorder(app: App): Boolean = if (_favoritesContextualMode.value is FavoritesContextMode.ReorderPickPosition) {
        (_favoritesContextualMode.value as FavoritesContextMode.ReorderPickPosition).app.packageName != app.packageName
    } else false

    fun hideContextualMode() {
        _favoritesContextualMode.value = FavoritesContextMode.Closed
    }
}
