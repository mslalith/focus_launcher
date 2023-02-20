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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import dev.mslalith.focuslauncher.core.common.extensions.openNotificationShade
import dev.mslalith.focuslauncher.core.ui.extensions.onSwipeDown
import dev.mslalith.focuslauncher.feature.clock24.ClockWidget
import dev.mslalith.focuslauncher.feature.homepage.favorites.FavoritesList
import dev.mslalith.focuslauncher.feature.homepage.model.HomePadding
import dev.mslalith.focuslauncher.feature.homepage.model.LocalHomePadding
import dev.mslalith.focuslauncher.feature.lunarcalendar.detailsdialog.LunarPhaseDetailsDialog

@Composable
fun HomePage() {
    HomePage(homePageViewModel = hiltViewModel())
}

@Composable
internal fun HomePage(
    homePageViewModel: HomePageViewModel,
) {
    val context = LocalContext.current
    val homePageState by homePageViewModel.homePageState.collectAsState()
    val showMoonCalendarDetailsDialog by homePageViewModel.showMoonCalendarDetailsDialogStateFlow.collectAsState()

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
                    onMoonCalendarClick = homePageViewModel::showMoonCalendarDetailsDialog
                )
                Box(modifier = Modifier.weight(1f)) {
                    DecoratedQuote(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                FavoritesList(
                    favoritesList = homePageState.favoritesList,
                    addDefaultAppsToFavorites = homePageViewModel::addDefaultAppsIfRequired,
                    removeFromFavorites = homePageViewModel::removeFromFavorites,
                    reorderFavorite = homePageViewModel::reorderFavorite,
                    currentContextMode1 = homePageState.favoritesContextualMode,
                    isInContextualMode = homePageViewModel::isInContextualMode,
                    isReordering = homePageViewModel::isReordering,
                    hideContextualMode = homePageViewModel::hideContextualMode,
                    changeFavoritesContextMode = homePageViewModel::changeFavoritesContextMode,
                    isAppAboutToReorder = homePageViewModel::isAppAboutToReorder,
                    contentPadding = horizontalPadding
                )
                Spacer(modifier = Modifier.height(bottomPadding))
            }
        }
    }

    if (showMoonCalendarDetailsDialog) {
        LunarPhaseDetailsDialog(
            onClose = homePageViewModel::hideMoonCalendarDetailsDialog
        )
    }
}
