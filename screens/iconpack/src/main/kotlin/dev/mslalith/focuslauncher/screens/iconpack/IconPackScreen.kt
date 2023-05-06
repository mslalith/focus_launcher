package dev.mslalith.focuslauncher.screens.iconpack

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReusableContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.model.app.AppWithIcon
import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.ui.AppBarWithBackIcon
import dev.mslalith.focuslauncher.core.ui.RoundIcon
import dev.mslalith.focuslauncher.core.ui.StatusBarColor
import dev.mslalith.focuslauncher.core.ui.modifiers.horizontalFadeOutEdge
import dev.mslalith.focuslauncher.feature.appdrawerpage.apps.grid.PreviewAppsGrid
import dev.mslalith.focuslauncher.screens.iconpack.model.IconPackState
import dev.mslalith.focuslauncher.screens.iconpack.ui.IconPackItem
import kotlinx.coroutines.flow.collectLatest

@Composable
fun IconPackScreen(
    goBack: () -> Unit
) {
    IconPackScreenInternal(
        goBack = goBack
    )
}

@Composable
internal fun IconPackScreenInternal(
    iconPackViewModel: IconPackViewModel = hiltViewModel(),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun IconPackScreen(
    iconPackState: IconPackState,
    onIconPackClick: (IconPackType) -> Unit,
    onDoneClick: () -> Unit,
    goBack: () -> Unit
) {
    val density = LocalDensity.current
    var bottomSheetTopOffset by remember { mutableStateOf(value = 0.dp) }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Expanded
        )
    )

    LaunchedEffect(key1 = bottomSheetScaffoldState.bottomSheetState) {
        snapshotFlow { bottomSheetScaffoldState.bottomSheetState.requireOffset() }.collectLatest {
            density.run { bottomSheetTopOffset = it.toDp() }
        }
    }

    StatusBarColor(hasTopAppBar = true)

    BoxWithConstraints {
        val sheetHeight = density.run { constraints.maxHeight.toDp() - bottomSheetTopOffset }

        BottomSheetScaffold(
            scaffoldState = bottomSheetScaffoldState,
            containerColor = MaterialTheme.colorScheme.background,
            sheetSwipeEnabled = false,
            topBar = {
                AppBarWithBackIcon(
                    title = stringResource(id = R.string.icon_pack),
                    onBackPressed = goBack,
                    actions = {
                        RoundIcon(
                            iconRes = R.drawable.ic_check,
                            contentDescription = stringResource(id = R.string.done_icon),
                            enabled = iconPackState.canSave,
                            onClick = onDoneClick
                        )
                    }
                )
            },
            sheetContent = {
                IconPackListSheet(
                    modifier = Modifier.navigationBarsPadding(),
                    iconPackState = iconPackState,
                    onIconPackClick = onIconPackClick
                )
            }
        ) {
            PreviewAppsGrid(
                appsState = iconPackState.allApps,
                topSpacing = 16.dp,
                bottomSpacing = 16.dp,
                modifier = Modifier.padding(bottom = sheetHeight)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun IconPackListSheet(
    modifier: Modifier = Modifier,
    iconPackState: IconPackState,
    onIconPackClick: (IconPackType) -> Unit
) {
    val context = LocalContext.current

    val systemIconPackApp: AppWithIcon? = remember {
        context.getDrawable(R.drawable.ic_launcher)?.let { icon ->
            AppWithIcon(
                app = App(
                    name = context.getString(R.string.app_name),
                    displayName = context.getString(R.string.app_name),
                    packageName = context.packageName,
                    isSystem = false
                ),
                icon = icon,
            )
        }
    }

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .horizontalFadeOutEdge(
                width = 16.dp,
                color = MaterialTheme.colorScheme.surface
            )
            .padding(bottom = 12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            if (systemIconPackApp != null) {
                ReusableContent(key = systemIconPackApp.uniqueKey) {
                    IconPackItem(
                        appWithIcon = systemIconPackApp,
                        isSelected = iconPackState.iconPackType == IconPackType.System,
                        onClick = { onIconPackClick(IconPackType.System) }
                    )
                }
            }
        }
        items(
            items = iconPackState.iconPacks,
            key = { it.uniqueKey }
        ) { appWithIcon ->
            val customIconPackType = IconPackType.Custom(packageName = appWithIcon.app.packageName)

            IconPackItem(
                appWithIcon = appWithIcon,
                isSelected = iconPackState.iconPackType == customIconPackType,
                onClick = { onIconPackClick(customIconPackType) },
                modifier = Modifier.animateItemPlacement()
            )
        }
    }
}
