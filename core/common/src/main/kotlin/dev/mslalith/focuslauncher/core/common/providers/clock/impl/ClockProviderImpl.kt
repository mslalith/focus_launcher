package dev.mslalith.focuslauncher.core.common.providers.clock.impl

import dev.mslalith.focuslauncher.core.common.providers.clock.ClockProvider
import dev.mslalith.focuslauncher.core.lint.kover.IgnoreInKoverReport
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@IgnoreInKoverReport
internal class ClockProviderImpl @Inject constructor() : ClockProvider {
    @OptIn(ExperimentalTime::class)
    override fun now(): Instant = Clock.System.now()
}
