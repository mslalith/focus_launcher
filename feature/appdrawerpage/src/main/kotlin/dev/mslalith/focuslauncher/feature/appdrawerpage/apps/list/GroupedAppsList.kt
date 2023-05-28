package dev.mslalith.focuslauncher.feature.appdrawerpage.apps.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerIconViewType
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerItem
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun GroupedAppsList(
    modifier: Modifier,
    apps: ImmutableList<AppDrawerItem>,
    character: Char,
    appDrawerIconViewType: AppDrawerIconViewType,
    showAppGroupHeader: Boolean,
    onAppClick: (AppDrawerItem) -> Unit,
    onAppLongClick: (AppDrawerItem) -> Unit
) {
    Column(
        modifier = modifier.padding(top = 20.dp)
    ) {
        if (showAppGroupHeader) {
            CharacterHeader(character = character)
        }
        apps.forEach { app ->
            AppDrawerListItem(
                appDrawerItem = app,
                appDrawerIconViewType = appDrawerIconViewType,
                onClick = onAppClick,
                onLongClick = onAppLongClick
            )
        }
    }
}
