package dev.mslalith.focuslauncher.core.data.repository.settings

import dev.mslalith.focuslauncher.core.model.ClockAlignment
import kotlinx.coroutines.flow.Flow

interface ClockSettingsRepo {
    val showClock24Flow: Flow<Boolean>
    val clockAlignmentFlow: Flow<ClockAlignment>
    val clock24AnimationDurationFlow: Flow<Int>

    suspend fun toggleClock24()
    suspend fun updateClockAlignment(clockAlignment: ClockAlignment)
    suspend fun updateClock24AnimationDuration(duration: Int)
}
