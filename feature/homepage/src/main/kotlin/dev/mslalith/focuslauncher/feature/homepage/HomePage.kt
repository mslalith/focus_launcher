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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.overlay.LocalOverlayHost
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.circuitoverlay.bottomsheet.showBottomSheet
import dev.mslalith.focuslauncher.core.common.extensions.openNotificationShade
import dev.mslalith.focuslauncher.core.screens.BottomSheetScreen
import dev.mslalith.focuslauncher.core.screens.HomePageScreen
import dev.mslalith.focuslauncher.core.screens.LunarPhaseDetailsBottomSheetScreen
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.extensions.onSwipeDown
import dev.mslalith.focuslauncher.feature.clock24.widget.ClockWidgetUiComponent
import dev.mslalith.focuslauncher.feature.favorites.FavoritesListUiComponent
import dev.mslalith.focuslauncher.feature.homepage.model.HomePadding
import dev.mslalith.focuslauncher.feature.homepage.model.LocalHomePadding
import kotlinx.coroutines.launch

@CircuitInject(HomePageScreen::class, SingletonComponent::class)
@Composable
fun HomePage(
    state: HomePageState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val overlayHost = LocalOverlayHost.current

    fun showBottomSheet(screen: BottomSheetScreen<Unit>) {
        scope.launch { overlayHost.showBottomSheet(screen) }
    }


    fun openClockApp() {
        runCatching {
            val intent = Intent(AlarmClock.ACTION_SHOW_ALARMS)
            context.startActivity(intent)
        }
    }

    HomePage(
        state = state,
        onClockWidgetClick = ::openClockApp,
        onLunarCalendarWidgetClick = { showBottomSheet(screen = LunarPhaseDetailsBottomSheetScreen) },
        modifier = modifier
    )
}

@Composable
internal fun HomePage(
    state: HomePageState,
    onClockWidgetClick: () -> Unit,
    onLunarCalendarWidgetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    CompositionLocalProvider(LocalHomePadding provides HomePadding()) {
        val contentPaddingValues = LocalHomePadding.current.contentPaddingValues
        val horizontalPadding = contentPaddingValues.calculateStartPadding(layoutDirection = LayoutDirection.Ltr)
        val topPadding = contentPaddingValues.calculateTopPadding()
        val bottomPadding = contentPaddingValues.calculateBottomPadding()

        Box(
            modifier = modifier
                .fillMaxSize()
                .onSwipeDown(enabled = state.isPullDownNotificationShadeEnabled) { context.openNotificationShade() }
        ) {
            Column(
                verticalArrangement = Arrangement.Bottom
            ) {
                VerticalSpacer(spacing = topPadding)
                ClockWidgetUiComponent(
                    state = state.clockWidgetUiComponentState,
                    horizontalPadding = horizontalPadding,
                    onClick = onClockWidgetClick
                )
                DecoratedLunarCalendar(
                    state = state.lunarCalendarUiComponentState,
                    onClick = onLunarCalendarWidgetClick
                )
                Box(modifier = Modifier.weight(weight = 1f)) {
                    DecoratedQuote(
                        state = state.quoteForYouUiComponentState,
                        modifier = Modifier.align(alignment = Alignment.Center)
                    )
                }
                FavoritesListUiComponent(
                    state = state.favoritesListUiComponentState,
                    contentPadding = bottomPadding
                )
                VerticalSpacer(spacing = bottomPadding)
            }
        }
    }
}
