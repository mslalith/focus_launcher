package dev.mslalith.focuslauncher.core.common.providers.clock

import kotlinx.datetime.Instant

interface ClockProvider {
    fun now(): Instant
}
