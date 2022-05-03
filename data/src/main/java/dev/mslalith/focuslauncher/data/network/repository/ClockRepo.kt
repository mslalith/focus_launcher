package dev.mslalith.focuslauncher.data.network.repository

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import javax.inject.Inject

class ClockRepo @Inject constructor() {
    private val _currentInstantStateFlow = MutableStateFlow(Clock.System.now())
    val currentInstantStateFlow: StateFlow<Instant>
        get() = _currentInstantStateFlow

    init { refreshTime() }

    fun refreshTime() {
        _currentInstantStateFlow.value = Clock.System.now()
    }

    @VisibleForTesting
    fun refreshTime(instant: Instant) {
        _currentInstantStateFlow.value = instant
    }
}
