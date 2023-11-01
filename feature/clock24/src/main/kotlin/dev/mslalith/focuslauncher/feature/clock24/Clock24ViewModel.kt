package dev.mslalith.focuslauncher.feature.clock24

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.common.extensions.formatToTime
import dev.mslalith.focuslauncher.core.data.repository.ClockRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.ClockSettingsRepo
import dev.mslalith.focuslauncher.core.model.ClockAlignment
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_24_ANIMATION_DURATION
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_ALIGNMENT
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.Clock.DEFAULT_SHOW_CLOCK_24
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.Clock.DEFAULT_USE_24_HOUR
import dev.mslalith.focuslauncher.core.ui.extensions.launchInIO
import dev.mslalith.focuslauncher.core.ui.extensions.withinScope
import dev.mslalith.focuslauncher.feature.clock24.model.Clock24State
import javax.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf

@HiltViewModel
internal class Clock24ViewModel @Inject constructor(
    private val clockRepo: ClockRepo,
    private val clockSettingsRepo: ClockSettingsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    private val defaultClock24State = Clock24State(
        currentTime = "",
        showClock24 = DEFAULT_SHOW_CLOCK_24,
        use24Hour = DEFAULT_USE_24_HOUR,
        clockAlignment = DEFAULT_CLOCK_ALIGNMENT,
        clock24AnimationDuration = DEFAULT_CLOCK_24_ANIMATION_DURATION
    )

    val clock24State = flowOf(value = defaultClock24State)
        .combine(flow = clockSettingsRepo.showClock24Flow) { state, showClock24 ->
            state.copy(showClock24 = showClock24)
        }.combine(flow = clockSettingsRepo.use24HourFlow) { state, use24Hour ->
            state.copy(
                use24Hour = use24Hour,
                currentTime = clockRepo.currentInstantStateFlow.value.formatToTime(use24Hour = use24Hour)
            )
        }.combine(flow = clockSettingsRepo.clockAlignmentFlow) { state, clockAlignment ->
            state.copy(clockAlignment = clockAlignment)
        }.combine(flow = clockSettingsRepo.clock24AnimationDurationFlow) { state, clock24AnimationDuration ->
            state.copy(clock24AnimationDuration = clock24AnimationDuration)
        }.combine(flow = clockRepo.currentInstantStateFlow) { state, currentInstant ->
            state.copy(currentTime = currentInstant.formatToTime(use24Hour = state.use24Hour))
        }.withinScope(initialValue = defaultClock24State)

    fun refreshTime() = clockRepo.refreshTime()

    fun toggleClock24() {
        appCoroutineDispatcher.launchInIO {
            clockSettingsRepo.toggleClock24()
        }
    }

    fun toggleUse24Hour() {
        appCoroutineDispatcher.launchInIO {
            clockSettingsRepo.toggleUse24Hour()
        }
    }

    fun updateClockAlignment(clockAlignment: ClockAlignment) {
        appCoroutineDispatcher.launchInIO {
            clockSettingsRepo.updateClockAlignment(clockAlignment = clockAlignment)
        }
    }

    fun updateClock24AnimationDuration(duration: Int) {
        appCoroutineDispatcher.launchInIO {
            clockSettingsRepo.updateClock24AnimationDuration(duration = duration)
        }
    }
}
