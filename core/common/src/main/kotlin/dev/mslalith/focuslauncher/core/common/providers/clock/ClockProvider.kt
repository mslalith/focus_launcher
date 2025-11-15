package dev.mslalith.focuslauncher.core.common.providers.clock

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

interface ClockProvider {
    @OptIn(ExperimentalTime::class)
    fun now(): Instant
}
