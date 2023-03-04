package dev.mslalith.focuslauncher.core.common.providers.clock.test

import dev.mslalith.focuslauncher.core.common.extensions.formatToTime
import dev.mslalith.focuslauncher.core.common.providers.clock.ClockProvider
import javax.inject.Inject
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class TestClockProvider @Inject constructor() : ClockProvider {

    private var instant = Clock.System.now()

    override fun now(): Instant {
        return instant
    }

    fun setInstant(instant: Instant) {
        this.instant = instant
        instant.formatToTime()
    }
}
