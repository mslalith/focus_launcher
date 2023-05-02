package dev.mslalith.focuslauncher.screens.editfavorites.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.mslalith.focuslauncher.core.model.app.SelectedApp
import dev.mslalith.focuslauncher.core.ui.SelectableCheckboxItem

@Composable
internal fun FavoriteListItem(
    modifier: Modifier = Modifier,
    selectedApp: SelectedApp,
    onAppClick: () -> Unit
) {
    SelectableCheckboxItem(
        modifier = modifier,
        text = selectedApp.app.name,
        checked = selectedApp.isSelected,
        disabled = selectedApp.disabled,
        onClick = onAppClick
    )
}
