package dev.mslalith.focuslauncher.screens.hideapps.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.mslalith.focuslauncher.core.model.ConfirmSelectableItemType
import dev.mslalith.focuslauncher.core.model.app.SelectedHiddenApp
import dev.mslalith.focuslauncher.core.model.UiText
import dev.mslalith.focuslauncher.core.resources.R
import dev.mslalith.focuslauncher.core.ui.ConfirmSelectableItem
import dev.mslalith.focuslauncher.core.ui.SelectableCheckboxItem

@Composable
internal fun HiddenAppListItem(
    modifier: Modifier = Modifier,
    selectedHiddenApp: SelectedHiddenApp,
    isFavorite: Boolean,
    onAppClick: () -> Unit
) {
    val appName = selectedHiddenApp.app.displayName
    val confirmToHideMessage = stringResource(id = R.string.hide_favorite_app_message, appName)

    if (isFavorite) {
        ConfirmSelectableItem(
            modifier = modifier,
            text = appName,
            confirmMessage = confirmToHideMessage,
            itemType = ConfirmSelectableItemType.Checkbox(
                checked = selectedHiddenApp.isSelected
            ),
            confirmUiText = UiText.Resource(stringRes = R.string.yes_comma_hide),
            onConfirm = onAppClick
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
