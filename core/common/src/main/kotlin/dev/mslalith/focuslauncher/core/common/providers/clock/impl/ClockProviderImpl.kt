package dev.mslalith.focuslauncher.core.common.providers.clock.impl

import dev.mslalith.focuslauncher.core.common.providers.clock.ClockProvider
import dev.mslalith.focuslauncher.core.lint.kover.IgnoreInKoverReport
import javax.inject.Inject
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@IgnoreInKoverReport
internal class ClockProviderImpl @Inject constructor() : ClockProvider {
    override fun now(): Instant = Clock.System.now()
}
