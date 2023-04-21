package dev.mslalith.focuslauncher.screens.hideapps.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.mslalith.focuslauncher.core.resources.R
import dev.mslalith.focuslauncher.core.ui.ConfirmSelectableItem
import dev.mslalith.focuslauncher.core.ui.SelectableCheckboxItem
import dev.mslalith.focuslauncher.core.ui.model.ConfirmSelectableItemType
import dev.mslalith.focuslauncher.screens.hideapps.model.HiddenApp

@Composable
internal fun HiddenAppListItem(
    hiddenApp: HiddenApp,
    isFavorite: Boolean,
    onAppClick: () -> Unit
) {
    val appName = hiddenApp.app.name
    val confirmToHideMessage = stringResource(R.string.hide_favorite_app_message, appName)

    if (isFavorite) {
        ConfirmSelectableItem(
            text = appName,
            confirmMessage = confirmToHideMessage,
            itemType = ConfirmSelectableItemType.Checkbox(
                checked = hiddenApp.isSelected
            ),
            backgroundColor = MaterialTheme.colorScheme.surface,
            confirmText = "Yes, Hide",
            onConfirm = {
                if (it) onAppClick()
            }
        )
    } else {
        SelectableCheckboxItem(
            text = appName,
            checked = hiddenApp.isSelected,
            onClick = onAppClick
        )
    }
}
