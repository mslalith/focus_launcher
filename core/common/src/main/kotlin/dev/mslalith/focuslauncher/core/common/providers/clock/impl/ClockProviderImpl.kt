package dev.mslalith.focuslauncher.core.common.providers.clock.impl

import dev.mslalith.focuslauncher.core.common.providers.clock.ClockProvider
import javax.inject.Inject
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

internal class ClockProviderImpl @Inject constructor() : ClockProvider {
    override fun now(): Instant = Clock.System.now()
}
