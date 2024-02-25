package dev.mslalith.focuslauncher.feature.clock24.bottomsheet.clockwidgetsettings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.common.extensions.formatToTime
import dev.mslalith.focuslauncher.core.data.repository.ClockRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.ClockSettingsRepo
import dev.mslalith.focuslauncher.core.model.ClockAlignment
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_24_ANIMATION_DURATION
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_ALIGNMENT
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.Clock.DEFAULT_SHOW_CLOCK_24
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.Clock.DEFAULT_USE_24_HOUR
import dev.mslalith.focuslauncher.core.screens.ClockWidgetSettingsBottomSheetScreen
import dev.mslalith.focuslauncher.feature.clock24.bottomsheet.clockwidgetsettings.ClockWidgetSettingsBottomSheetUiEvent.ToggleClock24
import dev.mslalith.focuslauncher.feature.clock24.bottomsheet.clockwidgetsettings.ClockWidgetSettingsBottomSheetUiEvent.ToggleUse24Hour
import dev.mslalith.focuslauncher.feature.clock24.bottomsheet.clockwidgetsettings.ClockWidgetSettingsBottomSheetUiEvent.UpdateClock24AnimationDuration
import dev.mslalith.focuslauncher.feature.clock24.bottomsheet.clockwidgetsettings.ClockWidgetSettingsBottomSheetUiEvent.UpdateClockAlignment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@CircuitInject(ClockWidgetSettingsBottomSheetScreen::class, SingletonComponent::class)
class ClockWidgetSettingsBottomSheetPresenter @Inject constructor(
    private val clockRepo: ClockRepo,
    private val clockSettingsRepo: ClockSettingsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : Presenter<ClockWidgetSettingsBottomSheetState> {

    @Composable
    override fun present(): ClockWidgetSettingsBottomSheetState {
        val scope = rememberCoroutineScope()

        val currentInstant by clockRepo.currentInstantStateFlow.collectAsRetainedState()
        val showClock24 by clockSettingsRepo.showClock24Flow.collectAsRetainedState(initial = DEFAULT_SHOW_CLOCK_24)
        val use24Hour by clockSettingsRepo.use24HourFlow.collectAsRetainedState(initial = DEFAULT_USE_24_HOUR)
        val clockAlignment by clockSettingsRepo.clockAlignmentFlow.collectAsRetainedState(initial = DEFAULT_CLOCK_ALIGNMENT)
        val clock24AnimationDuration by clockSettingsRepo.clock24AnimationDurationFlow.collectAsRetainedState(initial = DEFAULT_CLOCK_24_ANIMATION_DURATION)

        val currentTime = remember(key1 = currentInstant, key2 = use24Hour) {
            clockRepo.currentInstantStateFlow.value.formatToTime(use24Hour = use24Hour)
        }

        return ClockWidgetSettingsBottomSheetState(
            currentTime = currentTime,
            showClock24 = showClock24,
            use24Hour = use24Hour,
            clockAlignment = clockAlignment,
            clock24AnimationDuration = clock24AnimationDuration
        ) {
            when (it) {
                ToggleClock24 -> scope.toggleClock24()
                ToggleUse24Hour -> scope.toggleUse24Hour()
                is UpdateClock24AnimationDuration -> scope.updateClock24AnimationDuration(duration = it.duration)
                is UpdateClockAlignment -> scope.updateClockAlignment(clockAlignment = it.clockAlignment)
            }
        }
    }

    private fun CoroutineScope.toggleClock24() {
        launch(appCoroutineDispatcher.io) {
            clockSettingsRepo.toggleClock24()
        }
    }

    private fun CoroutineScope.toggleUse24Hour() {
        launch(appCoroutineDispatcher.io) {
            clockSettingsRepo.toggleUse24Hour()
        }
    }

    private fun CoroutineScope.updateClockAlignment(clockAlignment: ClockAlignment) {
        launch(appCoroutineDispatcher.io) {
            clockSettingsRepo.updateClockAlignment(clockAlignment = clockAlignment)
        }
    }

    private fun CoroutineScope.updateClock24AnimationDuration(duration: Int) {
        launch(appCoroutineDispatcher.io) {
            clockSettingsRepo.updateClock24AnimationDuration(duration = duration)
        }
    }
}
