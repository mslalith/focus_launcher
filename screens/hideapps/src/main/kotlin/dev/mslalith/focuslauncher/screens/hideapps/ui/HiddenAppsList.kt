package dev.mslalith.focuslauncher.screens.hideapps.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.screens.hideapps.model.HiddenApp

@Composable
internal fun HiddenAppsList(
    modifier: Modifier = Modifier,
    hiddenApps: List<HiddenApp>,
    contentPadding: PaddingValues = PaddingValues(),
    onRemoveFromFavorites: (App) -> Unit,
    onAddToHiddenApps: (App) -> Unit,
    onRemoveFromHiddenApps: (App) -> Unit
) {
    fun toggleHiddenApp(hiddenApp: HiddenApp) {
        if (hiddenApp.isSelected) {
            onRemoveFromHiddenApps(hiddenApp.app)
        } else {
            onAddToHiddenApps(hiddenApp.app)
            onRemoveFromFavorites(hiddenApp.app)
        }
    }

    LazyColumn(
        modifier = modifier.background(color = MaterialTheme.colorScheme.surface),
        contentPadding = contentPadding
    ) {
        items(
            items = hiddenApps
        ) { hiddenApp ->
            HiddenAppListItem(
                hiddenApp = hiddenApp,
                isFavorite = hiddenApp.isFavorite,
                onAppClick = { toggleHiddenApp(hiddenApp) }
            )
        }
        item { Spacer(Modifier.height(80.dp)) }
    }
}
