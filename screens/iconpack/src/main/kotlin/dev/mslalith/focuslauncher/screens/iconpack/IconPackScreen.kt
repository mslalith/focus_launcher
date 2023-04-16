package dev.mslalith.focuslauncher.screens.iconpack

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.ui.AppBarWithBackIcon
import dev.mslalith.focuslauncher.core.ui.RoundIcon
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.model.AppWithIcon
import dev.mslalith.focuslauncher.core.ui.model.IconType
import dev.mslalith.focuslauncher.feature.appdrawerpage.apps.grid.PreviewAppsGrid
import dev.mslalith.focuslauncher.screens.iconpack.model.IconPackState

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
            PreviewAppsGrid(
                apps = iconPackState.allApps,
                modifier = Modifier.weight(weight = 1f)
            )
            VerticalSpacer(spacing = 12.dp)
            LazyRow {
                items(
                    items = iconPackState.iconPacks,
                    key = { it.packageName }
                ) { app ->
                    Column(
                        modifier = Modifier
                            .clickable { onIconPackClick(app) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        // val iconBitmap = remember(key1 = app.packageName) {
                        //     app.icon.toBitmap().asImageBitmap()
                        // }
                        //
                        // Box(
                        //     modifier = Modifier.size(size = 28.dp * 1.5f)
                        // ) {
                        //     Image(
                        //         bitmap = iconBitmap,
                        //         contentDescription = app.displayName,
                        //         modifier = Modifier
                        //             .fillMaxSize()
                        //     )
                        // }
                        // VerticalSpacer(spacing = 8.dp)
                        Text(
                            text = app.displayName,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(
                                color = MaterialTheme.colors.onBackground,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
