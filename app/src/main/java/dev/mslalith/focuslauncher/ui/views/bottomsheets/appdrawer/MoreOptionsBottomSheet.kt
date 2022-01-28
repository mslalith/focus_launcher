package dev.mslalith.focuslauncher.ui.views.bottomsheets.appdrawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mslalith.focuslauncher.R
import dev.mslalith.focuslauncher.data.models.MoreAppOptionsProperties
import dev.mslalith.focuslauncher.extensions.showAppInfo
import dev.mslalith.focuslauncher.extensions.uninstallApp
import dev.mslalith.focuslauncher.extensions.verticalSpacer
import dev.mslalith.focuslauncher.ui.views.ConfirmSelectableItem
import dev.mslalith.focuslauncher.ui.views.ConfirmSelectableItemType
import dev.mslalith.focuslauncher.ui.views.SelectableIconItem
import kotlinx.coroutines.runBlocking

@Composable
fun MoreOptionsBottomSheet(
    properties: MoreAppOptionsProperties,
) {
    properties.apply {
        val context = LocalContext.current

        val isFavorite = runBlocking { appsViewModel.isFavorite(app.packageName) }
        val colors = MaterialTheme.colors

        val confirmToHideMessage = stringResource(R.string.hide_favorite_app_message, app.name)

        fun closeAfterAction(action: () -> Unit) {
            action()
            onClose()
        }


        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            12.dp.verticalSpacer()
            Text(
                text = app.name,
                style = TextStyle(
                    color = colors.onBackground,
                    fontSize = 20.sp
                ),
                modifier = Modifier.padding(top = 18.dp, bottom = 12.dp)
            )
            Divider(
                color = colors.onBackground,
                modifier = Modifier.fillMaxWidth(0.4f)
            )
            12.dp.verticalSpacer()

            val favoriteIconRes = if (isFavorite) R.drawable.ic_star_outline else R.drawable.ic_star
            SelectableIconItem(
                text = if (isFavorite) "Remove from Favorites" else "Add to Favorites",
                iconRes = favoriteIconRes,
                onClick = {
                    closeAfterAction {
                        appsViewModel.apply {
                            val app = app.toApp()
                            if (isFavorite) removeFromFavorites(app)
                            else addToFavorites(app)
                        }
                    }
                },
            )
            if (isFavorite) {
                ConfirmSelectableItem(
                    text = "Hide App",
                    confirmMessage = confirmToHideMessage,
                    itemType = ConfirmSelectableItemType.Icon(
                        iconRes = R.drawable.ic_visibility_off,
                    ),
                    confirmText = "Yes, Hide",
                    onConfirm = {
                        closeAfterAction {
                            if (it) appsViewModel.addToHiddenApps(app.toApp())
                        }
                    },
                )
            } else {
                SelectableIconItem(
                    text = "Hide App",
                    iconRes = R.drawable.ic_visibility_off,
                    onClick = {
                        closeAfterAction { appsViewModel.addToHiddenApps(app.toApp()) }
                    }
                )
            }
            SelectableIconItem(
                text = "App Info",
                iconRes = R.drawable.ic_info,
                onClick = {
                    closeAfterAction { context.showAppInfo(app.packageName) }
                }
            )

            if (!app.isSystem) {
                SelectableIconItem(
                    text = "Uninstall",
                    iconRes = R.drawable.ic_uninstall,
                    onClick = {
                        closeAfterAction { context.uninstallApp(app.toApp()) }
                    }
                )
            }
            bottomSpacing.verticalSpacer()
        }
    }
}
