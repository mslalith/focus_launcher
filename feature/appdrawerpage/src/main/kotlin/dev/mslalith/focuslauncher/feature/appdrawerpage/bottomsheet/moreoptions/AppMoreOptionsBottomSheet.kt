package dev.mslalith.focuslauncher.feature.appdrawerpage.bottomsheet.moreoptions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.common.extensions.showAppInfo
import dev.mslalith.focuslauncher.core.common.extensions.uninstallApp
import dev.mslalith.focuslauncher.core.lint.detekt.IgnoreLongMethod
import dev.mslalith.focuslauncher.core.model.ConfirmSelectableItemType
import dev.mslalith.focuslauncher.core.model.UiText
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerItem
import dev.mslalith.focuslauncher.core.screens.AppMoreOptionsBottomSheetScreen
import dev.mslalith.focuslauncher.core.ui.ConfirmSelectableItem
import dev.mslalith.focuslauncher.core.ui.SelectableIconItem
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.feature.appdrawerpage.R

@CircuitInject(AppMoreOptionsBottomSheetScreen::class, SingletonComponent::class)
@Composable
fun AppMoreOptionsBottomSheet(
    state: AppMoreOptionsBottomSheetState,
    modifier: Modifier = Modifier
) {
    // Need to extract the eventSink out to a local val, so that the Compose Compiler
    // treats it as stable. See: https://issuetracker.google.com/issues/256100927
    val eventSink = state.eventSink

    AppMoreOptionsBottomSheet(
        modifier = modifier,
        appDrawerItem = state.appDrawerItem,
        addToFavorites = { eventSink(AppMoreOptionsBottomSheetUiEvent.AddToFavorites(app = it)) },
        removeFromFavorites = { eventSink(AppMoreOptionsBottomSheetUiEvent.RemoveFromFavorites(app = it)) },
        addToHiddenApps = { app, removeFromFavorites -> eventSink(AppMoreOptionsBottomSheetUiEvent.AddToHiddenApps(app = app, removeFromFavorites = removeFromFavorites)) },
        onUpdateDisplayNameClick = { TODO() },
        onClose = { eventSink(AppMoreOptionsBottomSheetUiEvent.GoBack) }
    )
}

@Composable
@IgnoreLongMethod
private fun AppMoreOptionsBottomSheet(
    appDrawerItem: AppDrawerItem,
    addToFavorites: (App) -> Unit,
    removeFromFavorites: (App) -> Unit,
    addToHiddenApps: (App, Boolean) -> Unit,
    onUpdateDisplayNameClick: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val contentColor = MaterialTheme.colorScheme.onSurface

    val confirmToHideMessage = stringResource(id = R.string.hide_favorite_app_message, appDrawerItem.app.displayName)

    fun closeAfterAction(action: () -> Unit) {
        action()
        onClose()
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = appDrawerItem.app.displayName,
            color = contentColor,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Divider(
            color = contentColor,
            modifier = Modifier.fillMaxWidth(fraction = 0.4f)
        )
        VerticalSpacer(spacing = 16.dp)

        SelectableIconItem(
            text = stringResource(id = if (appDrawerItem.isFavorite) R.string.remove_from_favorites else R.string.add_to_favorites),
            iconRes = if (appDrawerItem.isFavorite) R.drawable.ic_star_outline else R.drawable.ic_star,
            contentColor = contentColor,
            onClick = {
                if (appDrawerItem.isFavorite) {
                    removeFromFavorites(appDrawerItem.app)
                } else {
                    addToFavorites(appDrawerItem.app)
                }
            }
        )
        if (appDrawerItem.isFavorite) {
            ConfirmSelectableItem(
                text = stringResource(id = R.string.hide_app),
                confirmMessage = confirmToHideMessage,
                itemType = ConfirmSelectableItemType.Icon(
                    iconRes = R.drawable.ic_visibility_off
                ),
                contentColor = contentColor,
                confirmUiText = UiText.Resource(stringRes = R.string.yes_comma_hide),
                onConfirm = { addToHiddenApps(appDrawerItem.app, true) }
            )
        } else {
            SelectableIconItem(
                text = stringResource(id = R.string.hide_app),
                iconRes = R.drawable.ic_visibility_off,
                contentColor = contentColor,
                onClick = { addToHiddenApps(appDrawerItem.app, false) }
            )
        }
        if (false) {
            SelectableIconItem(
                text = stringResource(id = R.string.update_display_name),
                iconRes = R.drawable.ic_edit,
                contentColor = contentColor,
                onClick = onUpdateDisplayNameClick
            )
        }
        SelectableIconItem(
            text = stringResource(id = R.string.app_info),
            iconRes = R.drawable.ic_info,
            contentColor = contentColor,
            onClick = {
                closeAfterAction { context.showAppInfo(packageName = appDrawerItem.app.packageName) }
            }
        )

        if (!appDrawerItem.app.isSystem) {
            SelectableIconItem(
                text = stringResource(id = R.string.uninstall),
                iconRes = R.drawable.ic_delete,
                contentColor = contentColor,
                onClick = {
                    closeAfterAction { context.uninstallApp(app = appDrawerItem.app) }
                }
            )
        }
        VerticalSpacer(spacing = 12.dp)
    }
}
