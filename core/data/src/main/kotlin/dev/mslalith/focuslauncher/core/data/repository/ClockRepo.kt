package dev.mslalith.focuslauncher.core.data.repository

import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Instant

interface ClockRepo {
    val currentInstantStateFlow: StateFlow<Instant>
    fun refreshTime()
}
