package dev.mslalith.focuslauncher.feature.homepage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.common.extensions.openNotificationShade
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.extensions.onSwipeDown
import dev.mslalith.focuslauncher.feature.clock24.ClockWidget
import dev.mslalith.focuslauncher.feature.favorites.FavoritesList
import dev.mslalith.focuslauncher.feature.homepage.model.HomePadding
import dev.mslalith.focuslauncher.feature.homepage.model.HomePageState
import dev.mslalith.focuslauncher.feature.homepage.model.LocalHomePadding
import dev.mslalith.focuslauncher.feature.lunarcalendar.detailsdialog.LunarPhaseDetailsDialog

@Composable
fun HomePage() {
    HomePageInternal()
}

@Composable
internal fun HomePageInternal(
    homePageViewModel: HomePageViewModel = hiltViewModel()
) {
    MoonCalendarDetailsDialog(
        showMoonCalendarDetailsDialogProvider = homePageViewModel.showMoonCalendarDetailsDialogStateFlow.collectAsStateWithLifecycle().value,
        onHideMoonCalendarDetailsDialog = homePageViewModel::hideMoonCalendarDetailsDialog
    )

    HomePage(
        homePageState = homePageViewModel.homePageState.collectAsStateWithLifecycle().value,
        onMoonCalendarClick = homePageViewModel::showMoonCalendarDetailsDialog
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
    onMoonCalendarClick: () -> Unit
) {
    val context = LocalContext.current

    CompositionLocalProvider(LocalHomePadding provides HomePadding()) {
        val contentPaddingValues = LocalHomePadding.current.contentPaddingValues
        val horizontalPadding = contentPaddingValues.calculateStartPadding(layoutDirection = LayoutDirection.Ltr)
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
                VerticalSpacer(spacing = topPadding)
                ClockWidget(horizontalPadding = horizontalPadding)
                SpacedMoonCalendar(
                    onMoonCalendarClick = onMoonCalendarClick
                )
                Box(modifier = Modifier.weight(1f)) {
                    DecoratedQuote(
                        modifier = Modifier.align(alignment = Alignment.Center)
                    )
                }
                FavoritesList(contentPadding = horizontalPadding)
                VerticalSpacer(spacing = bottomPadding)
            }
        }
    }
}
