package dev.mslalith.focuslauncher.feature.homepage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_NOTIFICATION_SHADE
import dev.mslalith.focuslauncher.core.screens.HomePageScreen
import dev.mslalith.focuslauncher.feature.clock24.widget.ClockWidgetUiComponentPresenter
import javax.inject.Inject

@CircuitInject(HomePageScreen::class, SingletonComponent::class)
class HomePagePresenter @Inject constructor(
    private val generalSettingsRepo: GeneralSettingsRepo,
    private val clockWidgetUiComponentPresenter: ClockWidgetUiComponentPresenter
) : Presenter<HomePageState> {

    private var showMoonCalendarDetailsDialog by mutableStateOf(value = false)

    @Composable
    override fun present(): HomePageState {
        val isPullDownNotificationShadeEnabled by generalSettingsRepo.notificationShadeFlow.collectAsState(initial = DEFAULT_NOTIFICATION_SHADE)

        val clockWidgetUiComponentState = clockWidgetUiComponentPresenter.present()

        return HomePageState(
            isPullDownNotificationShadeEnabled = isPullDownNotificationShadeEnabled,
            showMoonCalendarDetailsDialog = showMoonCalendarDetailsDialog,
            clockWidgetUiComponentState = clockWidgetUiComponentState
        ) {
            when (it) {
                HomePageUiEvent.ShowMoonCalendar -> Unit
            }
        }
    }
}
