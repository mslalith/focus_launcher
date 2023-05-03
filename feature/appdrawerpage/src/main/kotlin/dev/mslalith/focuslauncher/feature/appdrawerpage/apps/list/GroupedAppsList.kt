package dev.mslalith.focuslauncher.feature.appdrawerpage.apps.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.model.app.AppWithIconFavorite
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun GroupedAppsList(
    modifier: Modifier,
    apps: ImmutableList<AppWithIconFavorite>,
    character: Char,
    showAppIcons: Boolean,
    showAppGroupHeader: Boolean,
    onAppClick: (AppWithIconFavorite) -> Unit,
    onAppLongClick: (AppWithIconFavorite) -> Unit
) {
    Column(
        modifier = modifier.padding(top = 20.dp)
    ) {
        if (showAppGroupHeader) {
            CharacterHeader(character = character)
        }
        apps.forEach { app ->
            AppDrawerListItem(
                appWithIconFavorite = app,
                showAppIcons = showAppIcons,
                onClick = onAppClick,
                onLongClick = onAppLongClick
            )
        }
    }
}
