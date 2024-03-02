package dev.mslalith.focuslauncher.feature.homepage

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import dev.mslalith.focuslauncher.feature.clock24.widget.ClockWidgetUiComponentState
import dev.mslalith.focuslauncher.feature.favorites.FavoritesListUiComponentState
import dev.mslalith.focuslauncher.feature.lunarcalendar.widget.LunarCalendarUiComponentState
import dev.mslalith.focuslauncher.feature.quoteforyou.widget.QuoteForYouUiComponentState

data class HomePageState(
    val isPullDownNotificationShadeEnabled: Boolean,
    val clockWidgetUiComponentState: ClockWidgetUiComponentState,
    val lunarCalendarUiComponentState: LunarCalendarUiComponentState,
    val quoteForYouUiComponentState: QuoteForYouUiComponentState,
    val favoritesListUiComponentState: FavoritesListUiComponentState
) : CircuitUiState

sealed interface HomePageUiEvent : CircuitUiEvent
