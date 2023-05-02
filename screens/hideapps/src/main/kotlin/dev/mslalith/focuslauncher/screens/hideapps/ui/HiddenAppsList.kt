package dev.mslalith.focuslauncher.screens.hideapps.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.SelectedHiddenApp
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.testSemantics
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.screens.hideapps.utils.TestTags

@Composable
internal fun HiddenAppsList(
    modifier: Modifier = Modifier,
    selectedHiddenApps: List<SelectedHiddenApp>,
    contentPadding: PaddingValues = PaddingValues(),
    onRemoveFromFavorites: (App) -> Unit,
    onAddToHiddenApps: (App) -> Unit,
    onRemoveFromHiddenApps: (App) -> Unit
) {
    fun toggleHiddenApp(selectedHiddenApp: SelectedHiddenApp) {
        if (selectedHiddenApp.isSelected) {
            onRemoveFromHiddenApps(selectedHiddenApp.app)
        } else {
            onAddToHiddenApps(selectedHiddenApp.app)
            onRemoveFromFavorites(selectedHiddenApp.app)
        }
    }

    LazyColumn(
        modifier = modifier.testSemantics(tag = TestTags.TAG_HIDDEN_APPS_LIST),
        contentPadding = contentPadding
    ) {
        items(
            items = selectedHiddenApps
        ) { selectedHiddenApp ->
            HiddenAppListItem(
                modifier = Modifier
                    .testSemantics(tag = selectedHiddenApp.app.packageName)
                    .testSemantics(tag = TestTags.TAG_HIDDEN_APPS_LIST_ITEM) {
                        testSelectedHiddenApp(selectedHiddenApp = selectedHiddenApp)
                    },
                selectedHiddenApp = selectedHiddenApp,
                isFavorite = selectedHiddenApp.isFavorite,
                onAppClick = { toggleHiddenApp(selectedHiddenApp) }
            )
        }
        item { VerticalSpacer(spacing = 84.dp) }
    }
}
