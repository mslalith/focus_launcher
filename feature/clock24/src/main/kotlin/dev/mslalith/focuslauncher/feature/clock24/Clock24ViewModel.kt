package dev.mslalith.focuslauncher.feature.clock24

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.extensions.formatToTime
import dev.mslalith.focuslauncher.core.ui.extensions.withinScope
import dev.mslalith.focuslauncher.data.repository.ClockRepo
import dev.mslalith.focuslauncher.data.repository.settings.ClockSettingsRepo
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_24_ANIMATION_DURATION
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_ALIGNMENT
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.Clock.DEFAULT_SHOW_CLOCK_24
import dev.mslalith.focuslauncher.feature.clock24.model.Clock24State
import javax.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Clock

@HiltViewModel
internal class Clock24ViewModel @Inject constructor(
    private val clockRepo: ClockRepo,
    clockSettingsRepo: ClockSettingsRepo,
) : ViewModel() {

    private val defaultClock24State = Clock24State(
        currentTime = Clock.System.now().formatToTime(),
        showClock24 = DEFAULT_SHOW_CLOCK_24,
        clockAlignment = DEFAULT_CLOCK_ALIGNMENT,
        clock24AnimationDuration = DEFAULT_CLOCK_24_ANIMATION_DURATION
    )

    val clock24State = flowOf(defaultClock24State)
        .combine(clockSettingsRepo.showClock24Flow) { state, showClock24 ->
            state.copy(showClock24 = showClock24)
        }.combine(clockSettingsRepo.clockAlignmentFlow) { state, clockAlignment ->
            state.copy(clockAlignment = clockAlignment)
        }.combine(clockSettingsRepo.clock24AnimationDurationFlow) { state, clock24AnimationDuration ->
            state.copy(clock24AnimationDuration = clock24AnimationDuration)
        }.combine(clockRepo.currentInstantStateFlow) { state, currentInstant ->
            state.copy(currentTime = currentInstant.formatToTime())
        }.withinScope(initialValue = defaultClock24State)

    fun refreshTime() = clockRepo.refreshTime()
}
