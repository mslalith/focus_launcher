package dev.mslalith.focuslauncher.feature.homepage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_NOTIFICATION_SHADE
import dev.mslalith.focuslauncher.core.screens.HomePageScreen
import dev.mslalith.focuslauncher.feature.clock24.widget.ClockWidgetUiComponentPresenter
import dev.mslalith.focuslauncher.feature.favorites.FavoritesListUiComponentPresenter
import dev.mslalith.focuslauncher.feature.lunarcalendar.widget.LunarCalendarUiComponentPresenter
import dev.mslalith.focuslauncher.feature.quoteforyou.widget.QuoteForYouUiComponentPresenter
import javax.inject.Inject

@CircuitInject(HomePageScreen::class, SingletonComponent::class)
class HomePagePresenter @Inject constructor(
    private val generalSettingsRepo: GeneralSettingsRepo,
    private val clockWidgetUiComponentPresenter: ClockWidgetUiComponentPresenter,
    private val lunarCalendarUiComponentPresenter: LunarCalendarUiComponentPresenter,
    private val quoteForYouUiComponentPresenter: QuoteForYouUiComponentPresenter,
    private val favoritesListUiComponentPresenter: FavoritesListUiComponentPresenter
) : Presenter<HomePageState> {

    @Composable
    override fun present(): HomePageState {
        val isPullDownNotificationShadeEnabled by generalSettingsRepo.notificationShadeFlow.collectAsRetainedState(initial = DEFAULT_NOTIFICATION_SHADE)

        val clockWidgetUiComponentState = clockWidgetUiComponentPresenter.present()
        val lunarCalendarUiComponentState = lunarCalendarUiComponentPresenter.present()
        val quoteForYouUiComponentState = quoteForYouUiComponentPresenter.present()
        val favoritesListUiComponentState = favoritesListUiComponentPresenter.present()

        return HomePageState(
            isPullDownNotificationShadeEnabled = isPullDownNotificationShadeEnabled,
            clockWidgetUiComponentState = clockWidgetUiComponentState,
            lunarCalendarUiComponentState = lunarCalendarUiComponentState,
            quoteForYouUiComponentState = quoteForYouUiComponentState,
            favoritesListUiComponentState = favoritesListUiComponentState
        )
    }
}
