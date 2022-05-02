package dev.mslalith.focuslauncher.data.repository.interfaces

import androidx.annotation.VisibleForTesting
import dev.mslalith.focuslauncher.data.models.LunarPhase
import dev.mslalith.focuslauncher.data.models.LunarPhaseDetails
import dev.mslalith.focuslauncher.data.models.LunarPhaseDirection
import dev.mslalith.focuslauncher.data.models.Outcome
import dev.mslalith.focuslauncher.data.models.UpcomingLunarPhase
import dev.mslalith.focuslauncher.data.models.toLunarPhase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaInstant
import org.shredzone.commons.suncalc.MoonIllumination
import org.shredzone.commons.suncalc.MoonPhase

abstract class LunarPhaseDetailsRepo {
    abstract val currentTimeStateFlow: StateFlow<Outcome<String>>
    abstract val lunarPhaseDetailsStateFlow: StateFlow<Outcome<LunarPhaseDetails>>
    abstract val upcomingLunarPhaseStateFlow: StateFlow<Outcome<UpcomingLunarPhase>>

    abstract fun refreshTime()

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    fun findLunarPhaseDetails(instant: Instant): LunarPhaseDetails =
        MoonIllumination.compute().on(instant.toJavaInstant()).execute().run {
            LunarPhaseDetails(
                lunarPhase = closestPhase.toLunarPhase(),
                illumination = fraction,
                phaseAngle = phase,
            )
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    fun findUpcomingMoonPhaseFor(lunarPhaseDirection: LunarPhaseDirection) =
        when (lunarPhaseDirection) {
            LunarPhaseDirection.NEW_TO_FULL -> findUpcomingLunarPhaseOf(LunarPhase.FULL_MOON)
            LunarPhaseDirection.FULL_TO_NEW -> findUpcomingLunarPhaseOf(LunarPhase.NEW_MOON)
        }

    private fun findUpcomingLunarPhaseOf(lunarPhase: LunarPhase): UpcomingLunarPhase {
        val moonPhase = MoonPhase.compute().phase(lunarPhase.toMoonPhase()).execute()
        val localDateTime = try {
            val isoString = moonPhase.time.toString().substringBefore(delimiter = ".")
            LocalDateTime.parse(isoString)
        } catch (ex: Exception) {
            null
        }
        return UpcomingLunarPhase(
            lunarPhase = lunarPhase,
            dateTime = localDateTime,
            isMicroMoon = moonPhase.isMicroMoon,
            isSuperMoon = moonPhase.isSuperMoon,
        )
    }
}
