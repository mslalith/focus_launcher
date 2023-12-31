package dev.mslalith.focuslauncher.core.data.test.repository.settings

import dev.mslalith.focuslauncher.core.data.repository.settings.ClockSettingsRepo
import dev.mslalith.focuslauncher.core.model.ClockAlignment
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_24_ANIMATION_DURATION
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_ALIGNMENT
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.Clock.DEFAULT_SHOW_CLOCK_24
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.Clock.DEFAULT_USE_24_HOUR
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeClockSettingsRepo : ClockSettingsRepo {

    private val showClock24StateFlow = MutableStateFlow(value = DEFAULT_SHOW_CLOCK_24)
    override val showClock24Flow: Flow<Boolean> = showClock24StateFlow

    private val use24HourStateFlow = MutableStateFlow(value = DEFAULT_USE_24_HOUR)
    override val use24HourFlow: Flow<Boolean> = use24HourStateFlow

    private val clockAlignmentStateFlow = MutableStateFlow(value = DEFAULT_CLOCK_ALIGNMENT)
    override val clockAlignmentFlow: Flow<ClockAlignment> = clockAlignmentStateFlow

    private val clock24AnimationDurationStateFlow = MutableStateFlow(value = DEFAULT_CLOCK_24_ANIMATION_DURATION)
    override val clock24AnimationDurationFlow: Flow<Int> = clock24AnimationDurationStateFlow

    override suspend fun toggleClock24() {
        showClock24StateFlow.update { !it }
    }

    override suspend fun toggleUse24Hour() {
        use24HourStateFlow.update { !it }
    }

    override suspend fun updateClockAlignment(clockAlignment: ClockAlignment) {
        clockAlignmentStateFlow.update { clockAlignment }
    }

    override suspend fun updateClock24AnimationDuration(duration: Int) {
        clock24AnimationDurationStateFlow.update { duration }
    }
}
