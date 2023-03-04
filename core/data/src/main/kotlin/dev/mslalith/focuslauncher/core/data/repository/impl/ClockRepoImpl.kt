package dev.mslalith.focuslauncher.core.data.repository.impl

import dev.mslalith.focuslauncher.core.common.providers.clock.ClockProvider
import dev.mslalith.focuslauncher.core.data.repository.ClockRepo
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Instant

internal class ClockRepoImpl @Inject constructor(
    private val clockProvider: ClockProvider
) : ClockRepo {
    private val _currentInstantStateFlow = MutableStateFlow(clockProvider.now())
    override val currentInstantStateFlow: StateFlow<Instant>
        get() = _currentInstantStateFlow

    init { refreshTime() }

    override fun refreshTime() {
        _currentInstantStateFlow.value = clockProvider.now()
    }
}
