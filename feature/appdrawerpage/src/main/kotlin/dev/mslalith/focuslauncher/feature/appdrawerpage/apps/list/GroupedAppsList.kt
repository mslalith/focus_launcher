package dev.mslalith.focuslauncher.feature.appdrawerpage.apps.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.model.AppWithIcon

@Composable
internal fun GroupedAppsList(
    modifier: Modifier,
    apps: List<AppWithIcon>,
    character: Char,
    showAppIcons: Boolean,
    showAppGroupHeader: Boolean,
    onAppClick: (AppWithIcon) -> Unit,
    onAppLongClick: (AppWithIcon) -> Unit
) {
    Column(
        modifier = modifier.padding(top = 20.dp)
    ) {
        if (showAppGroupHeader) {
            CharacterHeader(character = character)
        }
        apps.forEach { app ->
            AppDrawerListItem(
                app = app,
                showAppIcons = showAppIcons,
                onClick = onAppClick,
                onLongClick = onAppLongClick
            )
        }
    }
}
