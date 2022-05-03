package dev.mslalith.focuslauncher.data.repository

import dev.mslalith.focuslauncher.data.models.Outcome
import dev.mslalith.focuslauncher.extensions.formatToTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import javax.inject.Inject

class ClockRepo @Inject constructor() {
    private val _currentTimeStateFlow = MutableStateFlow<Outcome<String>>(INITIAL_TIME_OUTCOME)
    val currentTimeStateFlow: StateFlow<Outcome<String>>
        get() = _currentTimeStateFlow

    private val _currentInstantStateFlow = MutableStateFlow(Clock.System.now())
    val currentInstantStateFlow: StateFlow<Instant>
        get() = _currentInstantStateFlow

    init { refreshTime() }

    fun refreshTime() {
        val currentInstant = Clock.System.now()
        _currentInstantStateFlow.value = currentInstant
        _currentTimeStateFlow.value = Outcome.Success(currentInstant.formatToTime())
    }

    companion object {
        val INITIAL_TIME_OUTCOME = Outcome.Error("")
    }
}
