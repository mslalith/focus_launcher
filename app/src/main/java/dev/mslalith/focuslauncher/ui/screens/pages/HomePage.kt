package dev.mslalith.focuslauncher.ui.screens.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import com.google.accompanist.flowlayout.FlowRow
import dev.mslalith.focuslauncher.data.models.AppWithIcon
import dev.mslalith.focuslauncher.extensions.defaultDialerApp
import dev.mslalith.focuslauncher.extensions.defaultMessagingApp
import dev.mslalith.focuslauncher.extensions.launchApp
import dev.mslalith.focuslauncher.extensions.luminate
import dev.mslalith.focuslauncher.extensions.onSwipeDown
import dev.mslalith.focuslauncher.extensions.openNotificationShade
import dev.mslalith.focuslauncher.extensions.toAppWithIconList
import dev.mslalith.focuslauncher.ui.viewmodels.AppsViewModel
import dev.mslalith.focuslauncher.ui.viewmodels.HomeViewModel
import dev.mslalith.focuslauncher.ui.viewmodels.SettingsViewModel
import dev.mslalith.focuslauncher.ui.viewmodels.WidgetsViewModel
import dev.mslalith.focuslauncher.ui.views.BackPressHandler
import dev.mslalith.focuslauncher.ui.views.widgets.ClockWidget
import dev.mslalith.focuslauncher.ui.views.widgets.LunarCalendar
import dev.mslalith.focuslauncher.ui.views.widgets.QuoteForYou
import kotlinx.coroutines.flow.first

private val LocalHomePadding = staticCompositionLocalOf<HomePadding> {
    error("No LocalHomePadding provided")
}

private data class HomePadding(
    val contentPaddingValues: PaddingValues = PaddingValues(
        start = 22.dp,
        end = 22.dp,
        top = 16.dp,
        bottom = 22.dp
    ),
    val lunarPhaseIconSize: Dp = 40.dp,
    val favoriteActionItemSize: Dp = 16.dp,
)

@Composable
fun HomePage(
    appsViewModel: AppsViewModel,
    homeViewModel: HomeViewModel,
    settingsViewModel: SettingsViewModel,
    widgetsViewModel: WidgetsViewModel,
) {
    val context = LocalContext.current
    val enablePullDownNotificationShade by settingsViewModel.notificationShadeStateFlow.collectAsState()

    CompositionLocalProvider(LocalHomePadding provides HomePadding()) {
        val contentPaddingValues = LocalHomePadding.current.contentPaddingValues
        val horizontalPadding = contentPaddingValues.calculateStartPadding(LayoutDirection.Ltr)
        val topPadding = contentPaddingValues.calculateTopPadding()
        val bottomPadding = contentPaddingValues.calculateBottomPadding()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .onSwipeDown(enabled = enablePullDownNotificationShade) { context.openNotificationShade() },
        ) {
            Column(
                verticalArrangement = Arrangement.Bottom
            ) {
                Spacer(modifier = Modifier.height(topPadding))
                ClockWidget(
                    settingsViewModel = settingsViewModel,
                    widgetsViewModel = widgetsViewModel,
                    horizontalPadding = horizontalPadding,
                )
                SpacedMoonCalendar(
                    settingsViewModel = settingsViewModel,
                    widgetsViewModel = widgetsViewModel,
                )
                Box(modifier = Modifier.weight(1f)) {
                    DecoratedQuote(
                        modifier = Modifier.align(Alignment.Center),
                        settingsViewModel = settingsViewModel,
                        widgetsViewModel = widgetsViewModel,
                    )
                }
                FavoritesList(
                    modifier = Modifier.padding(horizontal = horizontalPadding),
                    appsViewModel = appsViewModel,
                    homeViewModel = homeViewModel,
                    settingsViewModel = settingsViewModel,
                )
                Spacer(modifier = Modifier.height(bottomPadding))
            }
        }
    }
}

@Composable
private fun SpacedMoonCalendar(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel,
    widgetsViewModel: WidgetsViewModel,
) {
    val homePadding = LocalHomePadding.current
    val startPadding = homePadding.contentPaddingValues.calculateStartPadding(LayoutDirection.Ltr)
    val startOffsetPadding = startPadding - 16.dp
    val extraLunarPhaseIconSize = 1.dp
    val iconSize = homePadding.lunarPhaseIconSize + extraLunarPhaseIconSize

    Box(modifier = modifier) {
        LunarCalendar(
            settingsViewModel = settingsViewModel,
            widgetsViewModel = widgetsViewModel,
            iconSize = iconSize,
            horizontalPadding = startOffsetPadding,
        )
    }
}

@Composable
fun DecoratedQuote(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel,
    widgetsViewModel: WidgetsViewModel,
) {
    val homePadding = LocalHomePadding.current
    val startPadding = homePadding.contentPaddingValues.calculateStartPadding(LayoutDirection.Ltr)

    QuoteForYou(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = startPadding,
                vertical = 8.dp,
            ),
        settingsViewModel = settingsViewModel,
        widgetsViewModel = widgetsViewModel,
    )
}

@Composable
private fun FavoritesList(
    modifier: Modifier = Modifier,
    appsViewModel: AppsViewModel,
    homeViewModel: HomeViewModel,
    settingsViewModel: SettingsViewModel
) {
    val context = LocalContext.current
    val isInContextualMode by homeViewModel.isInContextualMode.collectAsState()
    val onlyFavoritesList by appsViewModel.onlyFavoritesStateFlow.collectAsState()

    fun onContextualModeChanged(value: Boolean) {
        homeViewModel.apply {
            when (value) {
                true -> showContextualMode()
                false -> hideContextualMode()
            }
        }
    }

    LaunchedEffect(onlyFavoritesList.isEmpty()) {
        if (onlyFavoritesList.isNotEmpty()) return@LaunchedEffect

        homeViewModel.hideContextualMode()
        appsViewModel.apply {
            val defaultApps = listOfNotNull(context.defaultDialerApp, context.defaultMessagingApp)
            if (defaultApps.isEmpty()) return@LaunchedEffect

            if (settingsViewModel.firstRunStateFlow.first()) {
                settingsViewModel.overrideFirstRun()
                defaultApps.forEach { addToFavorites(it) }
            }
        }
    }

    BackPressHandler(enabled = isInContextualMode) { homeViewModel.hideContextualMode() }

    FlowRow(
        modifier = modifier,
        mainAxisSpacing = 16.dp,
        crossAxisSpacing = 12.dp,
    ) {
        onlyFavoritesList.toAppWithIconList(context).forEach { favorite ->
            FavoriteItem(
                app = favorite,
                isInContextualMode = isInContextualMode,
                onContextualModeChange = ::onContextualModeChanged,
                onRemoveFavorite = { appsViewModel.removeFromFavorites(favorite.toApp()) }
            )
        }
    }
}

@Composable
fun FavoriteItem(
    app: AppWithIcon,
    isInContextualMode: Boolean,
    onContextualModeChange: (Boolean) -> Unit,
    onRemoveFavorite: () -> Unit
) {
    val context = LocalContext.current
    val onBackgroundColor = MaterialTheme.colors.onBackground

    fun openContextualMenu() = onContextualModeChange(true)

    val color = remember(key1 = app) {
        val appIconPalette = Palette.from(app.icon.toBitmap()).generate()
        val extractedColor = Color(appIconPalette.getDominantColor(onBackgroundColor.toArgb()))
        return@remember extractedColor.luminate(threshold = 0.36f, value = 0.6f)
    }
    val animatedColor by animateColorAsState(
        targetValue = color,
        animationSpec = tween(durationMillis = 600)
    )

    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .background(animatedColor.copy(alpha = 0.23f))
            .border(
                width = 0.25.dp,
                color = animatedColor,
                shape = MaterialTheme.shapes.small
            )
            .pointerInput(isInContextualMode) {
                if (isInContextualMode) return@pointerInput
                detectTapGestures(
                    onTap = { context.launchApp(app.toApp()) },
                    onLongPress = { openContextualMenu() }
                )
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = app.name,
            style = TextStyle(color = animatedColor),
        )
        AnimatedVisibility(visible = isInContextualMode) {
            FavoriteItemActionButton(
                icon = Icons.Rounded.Clear,
                contentDescription = "Remove Favorite",
                onClick = { onRemoveFavorite() }
            )
        }
    }
}

@Composable
private fun FavoriteItemActionButton(
    icon: ImageVector,
    contentDescription: String,
    backgroundColor: Color? = null,
    iconColor: Color? = null,
    onClick: () -> Unit,
) {
    val contentSize = LocalHomePadding.current.favoriteActionItemSize
    val contentColor = backgroundColor ?: MaterialTheme.colors.onBackground.copy(alpha = 0.23f)
    val iconTint = iconColor ?: MaterialTheme.colors.onBackground.copy(alpha = 0.6f)

    Box(
        modifier = Modifier
            .padding(start = 8.dp)
            .size(contentSize)
            .clip(CircleShape)
            .background(contentColor)
            .padding(2.dp)
            .clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconTint,
        )
    }
}
