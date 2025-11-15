package dev.mslalith.focuslauncher.core.data.repository

import kotlinx.coroutines.flow.StateFlow
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

interface ClockRepo {
    @OptIn(ExperimentalTime::class)
    val currentInstantStateFlow: StateFlow<Instant>
    fun refreshTime()
}
