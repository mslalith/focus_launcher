package dev.mslalith.focuslauncher.core.data.test.repository

import dev.mslalith.focuslauncher.core.data.repository.ClockRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class FakeClockRepo : ClockRepo {

    private val _currentInstantStateFlow = MutableStateFlow(value = Clock.System.now())
    override val currentInstantStateFlow: StateFlow<Instant> = _currentInstantStateFlow

    override fun refreshTime() = Unit

    fun setInstant(instant: Instant) {
        _currentInstantStateFlow.update { instant }
    }
}
