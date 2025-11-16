package dev.mslalith.focuslauncher.core.common.providers.clock.test

import dev.mslalith.focuslauncher.core.common.providers.clock.ClockProvider
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class TestClockProvider @Inject constructor() : ClockProvider {

    private var instant = Clock.System.now()

    override fun now(): Instant = instant

    fun setInstant(instant: Instant) {
        this.instant = instant
    }
}
