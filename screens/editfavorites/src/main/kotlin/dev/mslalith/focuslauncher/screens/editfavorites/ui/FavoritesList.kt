package dev.mslalith.focuslauncher.screens.editfavorites.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.model.SelectedApp
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.testSemantics
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.screens.editfavorites.R
import dev.mslalith.focuslauncher.screens.editfavorites.utils.TestTags

@Composable
internal fun FavoritesList(
    modifier: Modifier = Modifier,
    favorites: List<SelectedApp>,
    contentPadding: PaddingValues = PaddingValues(),
    showSnackbar: (String) -> Unit,
    onAddToFavorites: (App) -> Unit,
    onRemoveFromFavorites: (App) -> Unit
) {
    val context = LocalContext.current

    fun toggleFavorite(selectedApp: SelectedApp, isHidden: Boolean) {
        if (!isHidden) {
            if (selectedApp.isSelected) {
                onRemoveFromFavorites(selectedApp.app)
            } else {
                onAddToFavorites(selectedApp.app)
            }
        } else {
            showSnackbar(context.getString(R.string.app_hidden_message, selectedApp.app.name))
        }
    }

    LazyColumn(
        modifier = modifier.testSemantics(tag = TestTags.TAG_FAVORITES_LIST),
        contentPadding = contentPadding
    ) {
        items(
            items = favorites
        ) { favorite ->
            FavoriteListItem(
                modifier = Modifier
                    .testSemantics(tag = favorite.app.packageName)
                    .testSemantics(tag = TestTags.TAG_FAVORITES_LIST_ITEM) {
                        testSelectedApp(selectedApp = favorite)
                    },
                selectedApp = favorite,
                onAppClick = { toggleFavorite(selectedApp = favorite, isHidden = favorite.disabled) }
            )
        }
        item { VerticalSpacer(spacing = 84.dp) }
    }
}
