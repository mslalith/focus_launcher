package dev.mslalith.focuslauncher.feature.appdrawerpage.moreoptions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mslalith.focuslauncher.core.common.extensions.showAppInfo
import dev.mslalith.focuslauncher.core.common.extensions.toast
import dev.mslalith.focuslauncher.core.common.extensions.uninstallApp
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.ui.ConfirmSelectableItem
import dev.mslalith.focuslauncher.core.ui.SelectableIconItem
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.model.AppWithIcon
import dev.mslalith.focuslauncher.core.ui.model.ConfirmSelectableItemType
import dev.mslalith.focuslauncher.feature.appdrawerpage.R

@Composable
internal fun MoreOptionsBottomSheet(
    appWithIcon: AppWithIcon,
    isFavorite: suspend (App) -> Boolean,
    addToFavorites: (App) -> Unit,
    removeFromFavorites: (App) -> Unit,
    addToHiddenApps: (App) -> Unit,
    onUpdateDisplayNameClick: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val colors = MaterialTheme.colors

    val isFavoriteApp by produceState(initialValue = false, key1 = appWithIcon) {
        this.value = isFavorite(appWithIcon.toApp())
    }

    val confirmToHideMessage = stringResource(R.string.hide_favorite_app_message, appWithIcon.displayName)
    val addedAppToFavoritesMessage = stringResource(R.string.added_app_to_favorites, appWithIcon.displayName)
    val removedAppFromFavoritesMessage = stringResource(R.string.removed_app_from_favorites, appWithIcon.displayName)

    fun closeAfterAction(action: () -> Unit) {
        action()
        onClose()
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        VerticalSpacer(spacing = 24.dp)
        Text(
            text = appWithIcon.displayName,
            style = TextStyle(
                color = colors.onBackground,
                fontSize = 20.sp
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Divider(
            color = colors.onBackground,
            modifier = Modifier.fillMaxWidth(fraction = 0.4f)
        )
        VerticalSpacer(spacing = 16.dp)

        val favoriteIconRes = if (isFavoriteApp) R.drawable.ic_star_outline else R.drawable.ic_star
        SelectableIconItem(
            text = if (isFavoriteApp) "Remove from Favorites" else "Add to Favorites",
            iconRes = favoriteIconRes,
            onClick = {
                closeAfterAction {
                    if (isFavoriteApp) {
                        removeFromFavorites(appWithIcon.toApp())
                        context.toast(removedAppFromFavoritesMessage)
                    } else {
                        addToFavorites(appWithIcon.toApp())
                        context.toast(addedAppToFavoritesMessage)
                    }
                }
            }
        )
        if (isFavoriteApp) {
            ConfirmSelectableItem(
                text = "Hide App",
                confirmMessage = confirmToHideMessage,
                itemType = ConfirmSelectableItemType.Icon(
                    iconRes = R.drawable.ic_visibility_off
                ),
                confirmText = "Yes, Hide",
                onConfirm = {
                    closeAfterAction {
                        if (it) addToHiddenApps(appWithIcon.toApp())
                    }
                }
            )
        } else {
            SelectableIconItem(
                text = "Hide App",
                iconRes = R.drawable.ic_visibility_off,
                onClick = {
                    closeAfterAction { addToHiddenApps(appWithIcon.toApp()) }
                }
            )
        }
        SelectableIconItem(
            text = "Update Display Name",
            iconRes = R.drawable.ic_edit,
            onClick = {
                closeAfterAction { onUpdateDisplayNameClick() }
            }
        )
        SelectableIconItem(
            text = "App Info",
            iconRes = R.drawable.ic_info,
            onClick = {
                closeAfterAction { context.showAppInfo(appWithIcon.packageName) }
            }
        )

        if (!appWithIcon.isSystem) {
            SelectableIconItem(
                text = "Uninstall",
                iconRes = R.drawable.ic_delete,
                onClick = {
                    closeAfterAction { context.uninstallApp(appWithIcon.toApp()) }
                }
            )
        }
        VerticalSpacer(spacing = 12.dp)
    }
}
