package dev.mslalith.focuslauncher.screens.editfavourites.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.model.SelectedApp
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.testSemantics
import dev.mslalith.focuslauncher.core.ui.extensions.showSnackbar
import dev.mslalith.focuslauncher.screens.editfavourites.R
import dev.mslalith.focuslauncher.screens.editfavourites.utils.TestTags
import kotlinx.coroutines.launch

@Composable
internal fun FavoritesList(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
    favorites: List<SelectedApp>,
    onAddToFavorites: (App) -> Unit,
    onRemoveFromFavorites: (App) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val appHiddenMessage = stringResource(R.string.app_hidden_message)

    fun toggleFavorite(selectedApp: SelectedApp, isHidden: Boolean) {
        if (!isHidden) {
            if (selectedApp.isSelected) {
                onRemoveFromFavorites(selectedApp.app)
            } else {
                onAddToFavorites(selectedApp.app)
            }
        } else {
            coroutineScope.launch {
                scaffoldState.showSnackbar(
                    message = appHiddenMessage.replace("{}", selectedApp.app.name),
                    discardIfShowing = true
                )
            }
        }
    }

    LazyColumn(
        modifier = modifier.testSemantics(tag = TestTags.TAG_FAVOURITES_LIST)
    ) {
        items(
            items = favorites
        ) { favorite ->
            FavoriteListItem(
                modifier = Modifier.testSemantics(tag = TestTags.TAG_FAVOURITES_LIST_ITEM) {
                    testSelectedApp(selectedApp = favorite)
                },
                selectedApp = favorite,
                onAppClick = { toggleFavorite(favorite, favorite.disabled) }
            )
        }
        item { Spacer(Modifier.height(80.dp)) }
    }
}
