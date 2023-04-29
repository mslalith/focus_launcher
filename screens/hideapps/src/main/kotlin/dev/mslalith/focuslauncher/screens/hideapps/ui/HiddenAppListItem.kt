package dev.mslalith.focuslauncher.screens.hideapps.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.mslalith.focuslauncher.core.model.SelectedHiddenApp
import dev.mslalith.focuslauncher.core.resources.R
import dev.mslalith.focuslauncher.core.ui.ConfirmSelectableItem
import dev.mslalith.focuslauncher.core.ui.SelectableCheckboxItem
import dev.mslalith.focuslauncher.core.ui.model.ConfirmSelectableItemType

@Composable
internal fun HiddenAppListItem(
    modifier: Modifier = Modifier,
    selectedHiddenApp: SelectedHiddenApp,
    isFavorite: Boolean,
    onAppClick: () -> Unit
) {
    val appName = selectedHiddenApp.app.name
    val confirmToHideMessage = stringResource(id = R.string.hide_favorite_app_message, appName)

    if (isFavorite) {
        ConfirmSelectableItem(
            modifier = modifier,
            text = appName,
            confirmMessage = confirmToHideMessage,
            itemType = ConfirmSelectableItemType.Checkbox(
                checked = selectedHiddenApp.isSelected
            ),
            backgroundColor = MaterialTheme.colorScheme.surface,
            confirmText = "Yes, Hide",
            onConfirm = {
                if (it) onAppClick()
            }
        )
    } else {
        SelectableCheckboxItem(
            modifier = modifier,
            text = appName,
            checked = selectedHiddenApp.isSelected,
            onClick = onAppClick
        )
    }
}
