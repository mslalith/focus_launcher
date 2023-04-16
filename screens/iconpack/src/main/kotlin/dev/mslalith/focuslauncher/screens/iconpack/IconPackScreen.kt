package dev.mslalith.focuslauncher.screens.iconpack

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.ui.AppBarWithBackIcon
import dev.mslalith.focuslauncher.core.ui.RoundIcon
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.model.AppWithIcon
import dev.mslalith.focuslauncher.core.ui.model.IconType
import dev.mslalith.focuslauncher.feature.appdrawerpage.apps.grid.PreviewAppsGrid
import dev.mslalith.focuslauncher.screens.iconpack.model.IconPackState
import dev.mslalith.focuslauncher.screens.iconpack.ui.IconPackItem

@Composable
fun IconPackScreen(
    goBack: () -> Unit
) {
    IconPackScreen(
        iconPackViewModel = hiltViewModel(),
        goBack = goBack
    )
}

@Composable
internal fun IconPackScreen(
    iconPackViewModel: IconPackViewModel,
    goBack: () -> Unit
) {

    fun onDoneClick() {
        iconPackViewModel.saveIconPackType()
        goBack()
    }

    IconPackScreen(
        iconPackState = iconPackViewModel.iconPackState.collectAsStateWithLifecycle().value,
        onIconPackClick = iconPackViewModel::updateSelectedIconPackApp,
        onDoneClick = ::onDoneClick,
        goBack = goBack
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun IconPackScreen(
    iconPackState: IconPackState,
    onIconPackClick: (AppWithIcon) -> Unit,
    onDoneClick: () -> Unit,
    goBack: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            AppBarWithBackIcon(
                title = "Icon Pack",
                onBackPressed = goBack,
                actions = {
                    RoundIcon(
                        iconType = IconType.Vector(imageVector = Icons.Rounded.Done),
                        onClick = onDoneClick
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {
            val shape = MaterialTheme.shapes.large.copy(
                bottomStart = CornerSize(size = 0.dp),
                bottomEnd = CornerSize(size = 0.dp)
            )

            PreviewAppsGrid(
                apps = iconPackState.allApps,
                modifier = Modifier.weight(weight = 1f)
            )
            VerticalSpacer(spacing = 12.dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = shape)
                    .background(color = MaterialTheme.colors.secondaryVariant)
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                LazyRow {
                    items(
                        items = iconPackState.iconPacks,
                        key = { it.packageName.hashCode() + (31 * it.icon.hashCode()) }
                    ) { app ->
                        IconPackItem(
                            app = app,
                            onClick = { onIconPackClick(app) },
                            modifier = Modifier.animateItemPlacement()
                        )
                    }
                }
            }
        }
    }
}
