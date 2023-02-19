package dev.mslalith.focuslauncher.ui.screens.pages

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReusableContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import com.google.accompanist.flowlayout.FlowRow
import dev.mslalith.focuslauncher.R
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.ui.FillSpacer
import dev.mslalith.focuslauncher.core.ui.HorizontalSpacer
import dev.mslalith.focuslauncher.data.models.AppWithIcon
import dev.mslalith.focuslauncher.extensions.defaultDialerApp
import dev.mslalith.focuslauncher.extensions.defaultMessagingApp
import dev.mslalith.focuslauncher.extensions.launchApp
import dev.mslalith.focuslauncher.extensions.luminate
import dev.mslalith.focuslauncher.extensions.onSwipeDown
import dev.mslalith.focuslauncher.extensions.openNotificationShade
import dev.mslalith.focuslauncher.extensions.toAppWithIconList
import dev.mslalith.focuslauncher.feature.clock24.ClockWidget
import dev.mslalith.focuslauncher.ui.viewmodels.AppsViewModel
import dev.mslalith.focuslauncher.ui.viewmodels.FavoritesContextMode
import dev.mslalith.focuslauncher.ui.viewmodels.HomeViewModel
import dev.mslalith.focuslauncher.ui.viewmodels.SettingsViewModel
import dev.mslalith.focuslauncher.ui.viewmodels.WidgetsViewModel
import dev.mslalith.focuslauncher.ui.views.BackPressHandler
import dev.mslalith.focuslauncher.ui.views.IconType
import dev.mslalith.focuslauncher.ui.views.RoundIcon
import dev.mslalith.focuslauncher.ui.views.dialogs.LunarPhaseDetailsDialog
import dev.mslalith.focuslauncher.ui.views.widgets.LunarCalendar
import dev.mslalith.focuslauncher.ui.views.widgets.QuoteForYou
import kotlinx.coroutines.flow.first
import kotlin.reflect.KClass

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
    val favoriteActionItemSize: Dp = 16.dp
)

@Composable
fun HomePage(
    appsViewModel: AppsViewModel,
    homeViewModel: HomeViewModel,
    settingsViewModel: SettingsViewModel,
    widgetsViewModel: WidgetsViewModel
) {
    val context = LocalContext.current
    val enablePullDownNotificationShade by settingsViewModel.notificationShadeStateFlow.collectAsState()
    val showMoonCalendarDetailsDialog by widgetsViewModel.showMoonCalendarDetailsDialogStateFlow.collectAsState()

    CompositionLocalProvider(LocalHomePadding provides HomePadding()) {
        val contentPaddingValues = LocalHomePadding.current.contentPaddingValues
        val horizontalPadding = contentPaddingValues.calculateStartPadding(LayoutDirection.Ltr)
        val topPadding = contentPaddingValues.calculateTopPadding()
        val bottomPadding = contentPaddingValues.calculateBottomPadding()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .onSwipeDown(enabled = enablePullDownNotificationShade) { context.openNotificationShade() }
        ) {
            Column(
                verticalArrangement = Arrangement.Bottom
            ) {
                Spacer(modifier = Modifier.height(topPadding))
                ClockWidget(horizontalPadding = horizontalPadding)
                SpacedMoonCalendar(
                    settingsViewModel = settingsViewModel,
                    widgetsViewModel = widgetsViewModel,
                    onMoonCalendarClick = widgetsViewModel::showMoonCalendarDetailsDialog
                )
                Box(modifier = Modifier.weight(1f)) {
                    DecoratedQuote(
                        modifier = Modifier.align(Alignment.Center),
                        settingsViewModel = settingsViewModel,
                        widgetsViewModel = widgetsViewModel
                    )
                }
                // FavoritesList(
                //     appsViewModel = appsViewModel,
                //     homeViewModel = homeViewModel,
                //     settingsViewModel = settingsViewModel,
                //     contentPadding = horizontalPadding
                // )
                FavoritesListNew(
                    favoritesList = appsViewModel.onlyFavoritesStateFlow.collectAsState().value,
                    addDefaultAppsToFavorites = { defaultApps ->
                        if (settingsViewModel.firstRunStateFlow.first()) {
                            settingsViewModel.overrideFirstRun()
                            defaultApps.forEach { appsViewModel.addToFavorites(it) }
                        }
                    },
                    removeFromFavorites = appsViewModel::removeFromFavorites,
                    reorderFavorite = appsViewModel::reorderFavorite,
                    currentContextMode1 = homeViewModel.favoritesContextualMode.collectAsState().value,
                    isInContextualMode = homeViewModel::isInContextualMode,
                    isReordering = homeViewModel::isReordering,
                    hideContextualMode = homeViewModel::hideContextualMode,
                    changeFavoritesContextMode = homeViewModel::changeFavoritesContextMode,
                    isAppAboutToReorder = homeViewModel::isAppAboutToReorder,
                    contentPadding = horizontalPadding
                )
                Spacer(modifier = Modifier.height(bottomPadding))
            }
        }
    }

    if (showMoonCalendarDetailsDialog) {
        LunarPhaseDetailsDialog(
            widgetsViewModel = widgetsViewModel,
            onClose = widgetsViewModel::hideMoonCalendarDetailsDialog
        )
    }
}

@Composable
private fun SpacedMoonCalendar(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel,
    widgetsViewModel: WidgetsViewModel,
    onMoonCalendarClick: () -> Unit
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
            onClick = onMoonCalendarClick
        )
    }
}

@Composable
private fun DecoratedQuote(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel,
    widgetsViewModel: WidgetsViewModel
) {
    val homePadding = LocalHomePadding.current
    val startPadding = homePadding.contentPaddingValues.calculateStartPadding(LayoutDirection.Ltr)

    QuoteForYou(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = startPadding,
                vertical = 8.dp
            ),
        settingsViewModel = settingsViewModel,
        widgetsViewModel = widgetsViewModel
    )
}

@Composable
private fun FavoritesList(
    appsViewModel: AppsViewModel,
    homeViewModel: HomeViewModel,
    settingsViewModel: SettingsViewModel,
    contentPadding: Dp
) {
    val context = LocalContext.current
    val currentContextMode by homeViewModel.favoritesContextualMode.collectAsState()
    val onlyFavoritesList by appsViewModel.onlyFavoritesStateFlow.collectAsState()
    val favoritesWithAppIcon by remember {
        derivedStateOf {
            onlyFavoritesList.toAppWithIconList(context)
        }
    }

    LaunchedEffect(favoritesWithAppIcon.isEmpty()) {
        if (favoritesWithAppIcon.isNotEmpty() || homeViewModel.isReordering()) return@LaunchedEffect

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

    BackPressHandler(enabled = homeViewModel.isInContextualMode()) { homeViewModel.hideContextualMode() }

    val transition = updateTransition(targetState = currentContextMode, label = "Favorites Transition")
    val outerPadding by transition.animateDp(label = "Outer Padding") { if (it.isInContextualMode()) 16.dp else 0.dp }
    val innerPaddingBottom by transition.animateDp(label = "Inner Padding Bottom") { if (it.isInContextualMode()) 16.dp else 0.dp }
    val borderOpacity by transition.animateFloat(label = "Border Opacity") { if (it.isInContextualMode()) 0.8f else 0f }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = outerPadding)
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onBackground.copy(alpha = borderOpacity),
                shape = RoundedCornerShape(size = 12.dp)
            )
            .padding(bottom = innerPaddingBottom)
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            AnimatedVisibility(
                visible = homeViewModel.isInContextualMode(),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                FavoritesContextHeader(
                    currentContextMode = currentContextMode,
                    changeContextModeToOpen = { homeViewModel.changeFavoritesContextMode(FavoritesContextMode.Open) },
                    onReorderClick = { homeViewModel.changeFavoritesContextMode(FavoritesContextMode.Reorder) },
                    onRemoveClick = { homeViewModel.changeFavoritesContextMode(FavoritesContextMode.Remove) }
                )
            }
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = contentPadding),
                mainAxisSpacing = 16.dp,
                crossAxisSpacing = 12.dp
            ) {
                favoritesWithAppIcon.forEach { favorite ->
                    ReusableContent(key = favorite) {
                        FavoriteItem(
                            app = favorite,
                            homeViewModel = homeViewModel,
                            onClick = {
                                when (currentContextMode) {
                                    FavoritesContextMode.Closed -> context.launchApp(favorite.toApp())
                                    FavoritesContextMode.Open -> Unit
                                    FavoritesContextMode.Remove -> appsViewModel.removeFromFavorites(favorite.toApp())
                                    FavoritesContextMode.Reorder -> homeViewModel.changeFavoritesContextMode(FavoritesContextMode.ReorderPickPosition(favorite.toApp()))
                                    is FavoritesContextMode.ReorderPickPosition -> {
                                        val contextMode = currentContextMode as FavoritesContextMode.ReorderPickPosition
                                        appsViewModel.reorderFavorite(
                                            app = contextMode.app,
                                            withApp = favorite.toApp(),
                                            onReordered = { homeViewModel.changeFavoritesContextMode(FavoritesContextMode.Reorder) }
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoritesListNew(
    favoritesList: List<App>,
    addDefaultAppsToFavorites: suspend (List<App>) -> Unit,
    removeFromFavorites: (App) -> Unit,
    reorderFavorite: (App, App, () -> Unit) -> Unit,
    currentContextMode1: FavoritesContextMode,
    isInContextualMode: () -> Boolean,
    isReordering: () -> Boolean,
    hideContextualMode: () -> Unit,
    changeFavoritesContextMode: (FavoritesContextMode) -> Unit,
    isAppAboutToReorder: (App) -> Boolean,
    contentPadding: Dp
) {
    val context = LocalContext.current
    val currentContextMode by rememberUpdatedState(newValue = currentContextMode1)
    val favoritesWithAppIcon = remember(key1 = favoritesList) {
        // derivedStateOf {
        favoritesList.toAppWithIconList(context)
        // }
    }

    LaunchedEffect(favoritesWithAppIcon.isEmpty()) {
        if (favoritesWithAppIcon.isNotEmpty() || isReordering()) return@LaunchedEffect

        hideContextualMode()
        val defaultApps = listOfNotNull(context.defaultDialerApp, context.defaultMessagingApp)
        if (defaultApps.isNotEmpty()) addDefaultAppsToFavorites(defaultApps)
    }

    BackPressHandler(enabled = isInContextualMode()) { hideContextualMode() }

    val transition = updateTransition(targetState = currentContextMode, label = "Favorites Transition")
    val outerPadding by transition.animateDp(label = "Outer Padding") { if (it.isInContextualMode()) 16.dp else 0.dp }
    val innerPaddingBottom by transition.animateDp(label = "Inner Padding Bottom") { if (it.isInContextualMode()) 16.dp else 0.dp }
    val borderOpacity by transition.animateFloat(label = "Border Opacity") { if (it.isInContextualMode()) 0.8f else 0f }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = outerPadding)
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onBackground.copy(alpha = borderOpacity),
                shape = RoundedCornerShape(size = 12.dp)
            )
            .padding(bottom = innerPaddingBottom)
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            AnimatedVisibility(
                visible = isInContextualMode(),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                FavoritesContextHeader(
                    currentContextMode = currentContextMode,
                    changeContextModeToOpen = { changeFavoritesContextMode(FavoritesContextMode.Open) },
                    onReorderClick = { changeFavoritesContextMode(FavoritesContextMode.Reorder) },
                    onRemoveClick = { changeFavoritesContextMode(FavoritesContextMode.Remove) }
                )
            }
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = contentPadding),
                mainAxisSpacing = 16.dp,
                crossAxisSpacing = 12.dp
            ) {
                favoritesWithAppIcon.forEach { favorite ->
                    ReusableContent(key = favorite) {
                        FavoriteItemNew(
                            app = favorite,
                            isInContextualMode = isInContextualMode,
                            isAppAboutToReorder = { isAppAboutToReorder(favorite.toApp()) },
                            changeFavoritesContextMode = changeFavoritesContextMode,
                            onClick = {
                                when (currentContextMode) {
                                    FavoritesContextMode.Closed -> context.launchApp(favorite.toApp())
                                    FavoritesContextMode.Open -> Unit
                                    FavoritesContextMode.Remove -> removeFromFavorites(favorite.toApp())
                                    FavoritesContextMode.Reorder -> changeFavoritesContextMode(FavoritesContextMode.ReorderPickPosition(favorite.toApp()))
                                    is FavoritesContextMode.ReorderPickPosition -> {
                                        val reorderPickPosition = currentContextMode as FavoritesContextMode.ReorderPickPosition
                                        reorderFavorite(reorderPickPosition.app, favorite.toApp()) {
                                            changeFavoritesContextMode(FavoritesContextMode.Reorder)
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun FavoritesContextHeader(
    currentContextMode: FavoritesContextMode,
    changeContextModeToOpen: () -> Unit,
    onReorderClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    fun handleReClickFor(contextMode: FavoritesContextMode, action: () -> Unit) {
        if (currentContextMode == contextMode) changeContextModeToOpen() else action()
    }

    fun headerText(): String = when (currentContextMode) {
        FavoritesContextMode.Open, FavoritesContextMode.Closed -> "Favorites"
        FavoritesContextMode.Remove -> "Remove"
        FavoritesContextMode.Reorder, is FavoritesContextMode.ReorderPickPosition -> "Reorder"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(top = 12.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedContent(
            targetState = headerText(),
            transitionSpec = {
                slideInVertically { it } + fadeIn() with slideOutVertically { -it } + fadeOut() using SizeTransform(clip = false)
            }
        ) { header ->
            Text(
                text = header,
                style = TextStyle(
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 24.sp
                )
            )
        }
        FillSpacer()
        FavoritesContextActionItem(
            contextModes = listOf(FavoritesContextMode.Reorder::class, FavoritesContextMode.ReorderPickPosition::class) as List<KClass<FavoritesContextMode>>,
            currentContextMode = currentContextMode,
            iconType = IconType.Resource(resId = R.drawable.ic_drag_indicator),
            onClick = { handleReClickFor(FavoritesContextMode.Reorder) { onReorderClick() } }
        )
        HorizontalSpacer(spacing = 4.dp)
        FavoritesContextActionItem(
            contextModes = listOf(FavoritesContextMode.Remove::class) as List<KClass<FavoritesContextMode>>,
            currentContextMode = currentContextMode,
            iconType = IconType.Vector(imageVector = Icons.Default.Delete),
            onClick = { handleReClickFor(FavoritesContextMode.Remove) { onRemoveClick() } }
        )
    }
}

@Composable
private fun FavoritesContextActionItem(
    contextModes: List<KClass<FavoritesContextMode>>,
    currentContextMode: FavoritesContextMode,
    iconType: IconType,
    onClick: () -> Unit
) {
    val backgroundColor = MaterialTheme.colors.background
    val onBackgroundColor = MaterialTheme.colors.onBackground
    val iconBackgroundColor by animateColorAsState(
        targetValue = if (currentContextMode::class in contextModes) onBackgroundColor else backgroundColor,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )
    val iconColor by animateColorAsState(
        targetValue = if (currentContextMode::class in contextModes) backgroundColor else onBackgroundColor,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    RoundIcon(
        iconSize = 40.dp,
        iconType = iconType,
        backgroundColor = iconBackgroundColor,
        iconColor = iconColor,
        onClick = onClick
    )
}

@Composable
private fun FavoriteItem(
    app: AppWithIcon,
    homeViewModel: HomeViewModel,
    onClick: () -> Unit
) {
    val backgroundColor = MaterialTheme.colors.background
    val onBackgroundColor = MaterialTheme.colors.onBackground

    fun isAppAboutToReorder() = homeViewModel.isAppAboutToReorder(app.toApp())

    val color = remember(key1 = app) {
        val appIconPalette = Palette.from(app.icon.toBitmap()).generate()
        val extractedColor = Color(appIconPalette.getDominantColor(onBackgroundColor.toArgb()))
        return@remember extractedColor.luminate(threshold = 0.36f, value = 0.6f)
    }
    val animatedColor by animateColorAsState(
        targetValue = if (isAppAboutToReorder()) onBackgroundColor else color,
        animationSpec = tween(durationMillis = 600)
    )

    fun backgroundColor(): Color = animatedColor.copy(alpha = if (isAppAboutToReorder()) 0.8f else 0.23f)

    fun textColor(): Color = if (isAppAboutToReorder()) backgroundColor else animatedColor

    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .background(color = backgroundColor())
            .border(
                width = 0.25.dp,
                color = animatedColor,
                shape = MaterialTheme.shapes.small
            )
            .pointerInput(homeViewModel.isInContextualMode()) {
                detectTapGestures(
                    onTap = { onClick() },
                    onLongPress = {
                        if (homeViewModel.isInContextualMode()) return@detectTapGestures
                        homeViewModel.changeFavoritesContextMode(FavoritesContextMode.Open)
                    }
                )
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = app.displayName,
            style = TextStyle(color = textColor())
        )
    }
}

@Composable
private fun FavoriteItemNew(
    app: AppWithIcon,
    isInContextualMode: () -> Boolean,
    isAppAboutToReorder: () -> Boolean,
    changeFavoritesContextMode: (FavoritesContextMode) -> Unit,
    onClick: () -> Unit
) {
    val backgroundColor = MaterialTheme.colors.background
    val onBackgroundColor = MaterialTheme.colors.onBackground

    val color = remember(key1 = app) {
        val appIconPalette = Palette.from(app.icon.toBitmap()).generate()
        val extractedColor = Color(appIconPalette.getDominantColor(onBackgroundColor.toArgb()))
        return@remember extractedColor.luminate(threshold = 0.36f, value = 0.6f)
    }
    val animatedColor by animateColorAsState(
        targetValue = if (isAppAboutToReorder()) onBackgroundColor else color,
        animationSpec = tween(durationMillis = 600)
    )

    fun backgroundColor(): Color = animatedColor.copy(alpha = if (isAppAboutToReorder()) 0.8f else 0.23f)

    fun textColor(): Color = if (isAppAboutToReorder()) backgroundColor else animatedColor

    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .background(color = backgroundColor())
            .border(
                width = 0.25.dp,
                color = animatedColor,
                shape = MaterialTheme.shapes.small
            )
            .pointerInput(isInContextualMode()) {
                detectTapGestures(
                    onTap = { onClick() },
                    onLongPress = {
                        if (isInContextualMode()) return@detectTapGestures
                        changeFavoritesContextMode(FavoritesContextMode.Open)
                    }
                )
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = app.displayName,
            style = TextStyle(color = textColor())
        )
    }
}
