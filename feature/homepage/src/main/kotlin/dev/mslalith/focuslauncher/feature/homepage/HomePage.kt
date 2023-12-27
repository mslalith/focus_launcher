package dev.mslalith.focuslauncher.feature.homepage

import android.content.Intent
import android.provider.AlarmClock
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.common.extensions.openNotificationShade
import dev.mslalith.focuslauncher.core.screens.HomePageScreen
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.extensions.onSwipeDown
import dev.mslalith.focuslauncher.feature.homepage.model.HomePadding
import dev.mslalith.focuslauncher.feature.homepage.model.HomePageState
import dev.mslalith.focuslauncher.feature.homepage.model.LocalHomePadding
import dev.mslalith.focuslauncher.feature.lunarcalendar.detailsdialog.LunarPhaseDetailsDialog

@CircuitInject(HomePageScreen::class, SingletonComponent::class)
@Composable
fun HomePage(
    state: dev.mslalith.focuslauncher.feature.homepage.HomePageState,
    modifier: Modifier = Modifier
) {
    HomePage(
        homePageState = HomePageState(state.isPullDownNotificationShadeEnabled),
        onMoonCalendarClick = {},
        modifier = modifier
    )
}

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
    onMoonCalendarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    fun openClockApp() {
        val intent = Intent(AlarmClock.ACTION_SHOW_ALARMS)
        context.startActivity(intent)
    }

    CompositionLocalProvider(LocalHomePadding provides HomePadding()) {
        val contentPaddingValues = LocalHomePadding.current.contentPaddingValues
        val horizontalPadding = contentPaddingValues.calculateStartPadding(layoutDirection = LayoutDirection.Ltr)
        val topPadding = contentPaddingValues.calculateTopPadding()
        val bottomPadding = contentPaddingValues.calculateBottomPadding()

        Box(
            modifier = modifier
                .fillMaxSize()
                .onSwipeDown(enabled = homePageState.isPullDownNotificationShadeEnabled) { context.openNotificationShade() }
        ) {
            Column(
                verticalArrangement = Arrangement.Bottom
            ) {
                VerticalSpacer(spacing = topPadding)
                ClockWidget(
                    horizontalPadding = horizontalPadding,
                    onClick = ::openClockApp
                )
                SpacedMoonCalendar(
                    onMoonCalendarClick = onMoonCalendarClick
                )
                Box(modifier = Modifier.weight(weight = 1f)) {
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
