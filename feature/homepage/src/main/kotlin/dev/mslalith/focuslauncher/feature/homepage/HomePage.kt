package dev.mslalith.focuslauncher.feature.homepage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import dev.mslalith.focuslauncher.core.common.extensions.openNotificationShade
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.ui.extensions.onSwipeDown
import dev.mslalith.focuslauncher.feature.clock24.ClockWidget
import dev.mslalith.focuslauncher.feature.homepage.favorites.FavoritesList
import dev.mslalith.focuslauncher.feature.homepage.model.FavoritesContextMode
import dev.mslalith.focuslauncher.feature.homepage.model.HomePadding
import dev.mslalith.focuslauncher.feature.homepage.model.HomePageState
import dev.mslalith.focuslauncher.feature.homepage.model.LocalHomePadding
import dev.mslalith.focuslauncher.feature.lunarcalendar.detailsdialog.LunarPhaseDetailsDialog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomePage(
    pagerCurrentPage: Flow<Int>
) {
    HomePage(
        homePageViewModel = hiltViewModel(),
        pagerCurrentPage = pagerCurrentPage
    )
}

@Composable
internal fun HomePage(
    homePageViewModel: HomePageViewModel,
    pagerCurrentPage: Flow<Int>
) {
    LaunchedEffect(key1 = pagerCurrentPage) {
        pagerCurrentPage.collectLatest { page ->
            if (page != 1) homePageViewModel.hideContextualMode()
        }
    }

    MoonCalendarDetailsDialog(
        showMoonCalendarDetailsDialogProvider = homePageViewModel.showMoonCalendarDetailsDialogStateFlow.collectAsState().value,
        onHideMoonCalendarDetailsDialog = homePageViewModel::hideMoonCalendarDetailsDialog
    )

    HomePage(
        homePageState = homePageViewModel.homePageState.collectAsState().value,
        isInContextualMode = homePageViewModel::isInContextualMode,
        hideContextualMode = homePageViewModel::hideContextualMode,
        changeFavoritesContextMode = homePageViewModel::changeFavoritesContextMode,
        onMoonCalendarClick = homePageViewModel::showMoonCalendarDetailsDialog,
        addDefaultAppsIfRequired = homePageViewModel::addDefaultAppsIfRequired,
        removeFromFavorites = homePageViewModel::removeFromFavorites,
        reorderFavorite = homePageViewModel::reorderFavorite,
        isAppAboutToReorder = homePageViewModel::isAppAboutToReorder,
        isReordering = homePageViewModel::isReordering
    )
}

@Composable
internal fun MoonCalendarDetailsDialog(
    showMoonCalendarDetailsDialogProvider: Boolean,
    onHideMoonCalendarDetailsDialog: () -> Unit
) {
    if (showMoonCalendarDetailsDialogProvider) {
        LunarPhaseDetailsDialog(
            onClose = onHideMoonCalendarDetailsDialog
        )
    }
}

@Composable
internal fun HomePage(
    homePageState: HomePageState,
    isInContextualMode: () -> Boolean,
    hideContextualMode: () -> Unit,
    changeFavoritesContextMode: (FavoritesContextMode) -> Unit,
    onMoonCalendarClick: () -> Unit,
    addDefaultAppsIfRequired: (List<App>) -> Unit,
    removeFromFavorites: (App) -> Unit,
    reorderFavorite: (App, App, () -> Unit) -> Unit,
    isAppAboutToReorder: (App) -> Boolean,
    isReordering: () -> Boolean,
) {
    val context = LocalContext.current

    CompositionLocalProvider(LocalHomePadding provides HomePadding()) {
        val contentPaddingValues = LocalHomePadding.current.contentPaddingValues
        val horizontalPadding = contentPaddingValues.calculateStartPadding(LayoutDirection.Ltr)
        val topPadding = contentPaddingValues.calculateTopPadding()
        val bottomPadding = contentPaddingValues.calculateBottomPadding()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .onSwipeDown(enabled = homePageState.isPullDownNotificationShadeEnabled) { context.openNotificationShade() }
        ) {
            Column(
                verticalArrangement = Arrangement.Bottom
            ) {
                Spacer(modifier = Modifier.height(topPadding))
                ClockWidget(horizontalPadding = horizontalPadding)
                SpacedMoonCalendar(
                    onMoonCalendarClick = onMoonCalendarClick
                )
                Box(modifier = Modifier.weight(1f)) {
                    DecoratedQuote(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                FavoritesList(
                    favoritesList = homePageState.favoritesList,
                    addDefaultAppsToFavorites = addDefaultAppsIfRequired,
                    removeFromFavorites = removeFromFavorites,
                    reorderFavorite = reorderFavorite,
                    currentContextMode1 = homePageState.favoritesContextualMode,
                    isInContextualMode = isInContextualMode,
                    isReordering = isReordering,
                    hideContextualMode = hideContextualMode,
                    changeFavoritesContextMode = changeFavoritesContextMode,
                    isAppAboutToReorder = isAppAboutToReorder,
                    contentPadding = horizontalPadding
                )
                Spacer(modifier = Modifier.height(bottomPadding))
            }
        }
    }
}
