package dev.mslalith.focuslauncher.screens.iconpack

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.AppWithIcon
import dev.mslalith.focuslauncher.core.screens.IconPackScreen
import dev.mslalith.focuslauncher.core.ui.AppBarWithBackIcon
import dev.mslalith.focuslauncher.core.ui.RoundIcon
import dev.mslalith.focuslauncher.core.ui.StatusBarColor
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.extensions.clickableNoRipple
import dev.mslalith.focuslauncher.core.ui.modifiers.horizontalFadeOutEdge
import dev.mslalith.focuslauncher.feature.appdrawerpage.apps.grid.PreviewAppsGrid
import kotlinx.coroutines.flow.collectLatest

private val APP_ICON_SIZE = 28.dp

@CircuitInject(IconPackScreen::class, SingletonComponent::class)
@Composable
fun IconPack(
    state: IconPackState,
    modifier: Modifier = Modifier
) {
    // Need to extract the eventSink out to a local val, so that the Compose Compiler
    // treats it as stable. See: https://issuetracker.google.com/issues/256100927
    val eventSink = state.eventSink

    IconPackScreen(
        modifier = modifier,
        iconPackState = state,
        onIconPackClick = { eventSink(IconPackUiEvent.UpdateSelectedIconPackApp(iconPackType = it)) },
        onDoneClick = { eventSink(IconPackUiEvent.SaveIconPack) },
        goBack = { eventSink(IconPackUiEvent.GoBack) }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IconPackScreen(
    iconPackState: IconPackState,
    onIconPackClick: (IconPackType) -> Unit,
    onDoneClick: () -> Unit,
    goBack: () -> Unit,
    modifier: Modifier = Modifier
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

    StatusBarColor()

    BoxWithConstraints {
        val sheetHeight = density.run { constraints.maxHeight.toDp() - bottomSheetTopOffset }

        BottomSheetScaffold(
            modifier = modifier,
            scaffoldState = bottomSheetScaffoldState,
            containerColor = MaterialTheme.colorScheme.surface,
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
                icon = icon
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

@Composable
private fun IconPackItem(
    modifier: Modifier = Modifier,
    appWithIcon: AppWithIcon,
    isSelected: Boolean,
    onClick: (AppWithIcon) -> Unit
) {
    val iconBitmap = remember(key1 = appWithIcon.app.packageName) {
        appWithIcon.icon.toBitmap().asImageBitmap()
    }

    val backgroundColor by animateColorAsState(
        label = "Background Color",
        targetValue = if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent
    )

    val textColor by animateColorAsState(
        label = "Text Color",
        targetValue = if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurface
    )

    Column(
        modifier = modifier
            .width(intrinsicSize = IntrinsicSize.Min)
            .padding(horizontal = 4.dp, vertical = 0.dp)
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = backgroundColor)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickableNoRipple { onClick(appWithIcon) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var appName by remember { mutableStateOf(value = appWithIcon.app.displayName) }

        Box(
            modifier = Modifier.width(width = APP_ICON_SIZE * 1.5f)
        ) {
            Image(
                bitmap = iconBitmap,
                contentDescription = appName
            )
        }
        VerticalSpacer(spacing = 8.dp)
        Text(
            text = appName,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = textColor,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.animateContentSize(),
            onTextLayout = { result ->
                val emptyLinesCount = result.multiParagraph.run { maxLines - lineCount }
                if (emptyLinesCount > 0) {
                    appName = appName.plus("\n".repeat(n = emptyLinesCount))
                }
            }
        )
    }
}
