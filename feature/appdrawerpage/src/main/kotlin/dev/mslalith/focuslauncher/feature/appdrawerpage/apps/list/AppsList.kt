package dev.mslalith.focuslauncher.feature.appdrawerpage.apps.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.common.extensions.groupByImmutable
import dev.mslalith.focuslauncher.core.common.extensions.isAlphabet
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerIconViewType
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerItem
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun AppsList(
    apps: ImmutableList<AppDrawerItem>,
    appDrawerIconViewType: AppDrawerIconViewType,
    showAppGroupHeader: Boolean,
    isSearchQueryEmpty: Boolean,
    onAppClick: (AppDrawerItem) -> Unit,
    onAppLongClick: (AppDrawerItem) -> Unit
) {
    val configuration = LocalConfiguration.current
    val topSpacing = configuration.screenHeightDp.dp * 0.2f
    val bottomSpacing = configuration.screenHeightDp.dp * 0.05f

    val groupedApps by remember(key1 = apps) {
        derivedStateOf {
            apps.groupByImmutable { appModel ->
                appModel.app.displayName.first().let { if (it.isAlphabet()) it.uppercaseChar() else '#' }
            }
        }
    }

    val spacing = remember(key1 = isSearchQueryEmpty) {
        when (isSearchQueryEmpty) {
            true -> topSpacing to bottomSpacing
            false -> 0.dp to 0.dp
        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .height(height = 150.dp)
    ) {
        item { VerticalSpacer(spacing = spacing.first) }

        groupedApps.forEach { (character, apps) ->
            item(key = character) {
                GroupedAppsList(
                    apps = apps,
                    character = character,
                    appDrawerIconViewType = appDrawerIconViewType,
                    showAppGroupHeader = showAppGroupHeader && groupedApps.size != 1,
                    onAppClick = onAppClick,
                    onAppLongClick = onAppLongClick,
                    modifier = Modifier.animateItemPlacement()
                )
            }
        }
        item { VerticalSpacer(spacing = spacing.second) }
    }
}
