package dev.mslalith.focuslauncher.feature.clock24.widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.common.extensions.formatToTime
import dev.mslalith.focuslauncher.core.data.repository.ClockRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.ClockSettingsRepo
import dev.mslalith.focuslauncher.core.model.Constants
import dev.mslalith.focuslauncher.core.screens.ClockWidgetUiComponentScreen
import dev.mslalith.focuslauncher.feature.clock24.widget.ClockWidgetUiComponentUiEvent.RefreshTime
import javax.inject.Inject

@CircuitInject(ClockWidgetUiComponentScreen::class, SingletonComponent::class)
class ClockWidgetUiComponentPresenter @Inject constructor(
    private val clockRepo: ClockRepo,
    private val clockSettingsRepo: ClockSettingsRepo
) : Presenter<ClockWidgetUiComponentState> {

    @Composable
    override fun present(): ClockWidgetUiComponentState {
        val currentInstant by clockRepo.currentInstantStateFlow.collectAsState()
        val showClock24 by clockSettingsRepo.showClock24Flow.collectAsState(initial = Constants.Defaults.Settings.Clock.DEFAULT_SHOW_CLOCK_24)
        val use24Hour by clockSettingsRepo.use24HourFlow.collectAsState(initial = Constants.Defaults.Settings.Clock.DEFAULT_USE_24_HOUR)
        val clockAlignment by clockSettingsRepo.clockAlignmentFlow.collectAsState(initial = Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_ALIGNMENT)
        val clock24AnimationDuration by clockSettingsRepo.clock24AnimationDurationFlow.collectAsState(initial = Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_24_ANIMATION_DURATION)

        val currentTime = remember(key1 = currentInstant, key2 = use24Hour) {
            clockRepo.currentInstantStateFlow.value.formatToTime(use24Hour = use24Hour)
        }

        return ClockWidgetUiComponentState(
            currentTime = currentTime,
            showClock24 = showClock24,
            use24Hour = use24Hour,
            clockAlignment = clockAlignment,
            clock24AnimationDuration = clock24AnimationDuration
        ) {
            when (it) {
                RefreshTime -> clockRepo.refreshTime()
            }
        }
    }
}
