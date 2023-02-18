package dev.mslalith.focuslauncher.data.repository

import androidx.annotation.VisibleForTesting
import dev.mslalith.focuslauncher.core.common.State
import dev.mslalith.focuslauncher.core.common.extensions.toKotlinxLocalDateTime
import dev.mslalith.focuslauncher.core.model.City
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhase
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhaseDetails
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhaseDirection
import dev.mslalith.focuslauncher.core.model.lunarphase.NextPhaseDetails
import dev.mslalith.focuslauncher.core.model.lunarphase.RiseAndSetDetails
import dev.mslalith.focuslauncher.core.model.lunarphase.UpcomingLunarPhase
import dev.mslalith.focuslauncher.core.model.lunarphase.toLunarPhase
import javax.inject.Inject
import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import org.shredzone.commons.suncalc.MoonIllumination
import org.shredzone.commons.suncalc.MoonPhase
import org.shredzone.commons.suncalc.MoonTimes
import org.shredzone.commons.suncalc.SunTimes

class LunarPhaseDetailsRepo @Inject constructor() {
    private val _lunarPhaseDetailsStateFlow = MutableStateFlow<State<LunarPhaseDetails>>(
        INITIAL_LUNAR_PHASE_DETAILS_STATE
    )
    val lunarPhaseDetailsStateFlow: StateFlow<State<LunarPhaseDetails>>
        get() = _lunarPhaseDetailsStateFlow

    private val _upcomingLunarPhaseStateFlow = MutableStateFlow<State<UpcomingLunarPhase>>(
        INITIAL_UPCOMING_LUNAR_PHASE_STATE
    )
    val upcomingLunarPhaseStateFlow: StateFlow<State<UpcomingLunarPhase>>
        get() = _upcomingLunarPhaseStateFlow

    suspend fun refreshLunarPhaseDetails(instant: Instant, city: City) {
        val delayInMillis = Random.nextInt(from = 8, until = 17) * 100L
        delay(delayInMillis)

        val lunarPhaseDetails = findLunarPhaseDetails(instant, city)
        updateStateFlowsWith(lunarPhaseDetails)
    }

    private fun updateStateFlowsWith(lunarPhaseDetails: LunarPhaseDetails) {
        _lunarPhaseDetailsStateFlow.value = State.Success(lunarPhaseDetails)
        _upcomingLunarPhaseStateFlow.value =
            State.Success(findUpcomingMoonPhaseFor(lunarPhaseDetails.direction))
    }

    @VisibleForTesting
    fun findLunarPhaseDetails(instant: Instant, city: City): LunarPhaseDetails {
        val nextNewMoon = MoonPhase.compute().phase(MoonPhase.Phase.NEW_MOON).execute()
        val nextFullMoon = MoonPhase.compute().phase(MoonPhase.Phase.FULL_MOON).execute()
        val moonIllumination = MoonIllumination.compute().on(instant.toJavaInstant()).execute()
        val moonTimes = MoonTimes.compute().today().at(city.latitude, city.longitude).execute()
        val sunTimes = SunTimes.compute().today().at(city.latitude, city.longitude).execute()
        return LunarPhaseDetails(
            lunarPhase = moonIllumination.closestPhase.toLunarPhase(),
            illumination = moonIllumination.fraction,
            phaseAngle = moonIllumination.phase,
            nextPhaseDetails = NextPhaseDetails(
                newMoon = nextNewMoon.time?.toKotlinxLocalDateTime(),
                fullMoon = nextFullMoon.time?.toKotlinxLocalDateTime()
            ),
            moonRiseAndSetDetails = RiseAndSetDetails(
                riseDateTime = moonTimes.rise?.toKotlinxLocalDateTime(),
                setDateTime = moonTimes.set?.toKotlinxLocalDateTime()
            ),
            sunRiseAndSetDetails = RiseAndSetDetails(
                riseDateTime = sunTimes.rise?.toKotlinxLocalDateTime(),
                setDateTime = sunTimes.set?.toKotlinxLocalDateTime()
            )
        )
    }

    @VisibleForTesting
    fun findUpcomingMoonPhaseFor(lunarPhaseDirection: LunarPhaseDirection) =
        when (lunarPhaseDirection) {
            LunarPhaseDirection.NEW_TO_FULL -> findUpcomingLunarPhaseOf(LunarPhase.FULL_MOON)
            LunarPhaseDirection.FULL_TO_NEW -> findUpcomingLunarPhaseOf(LunarPhase.NEW_MOON)
        }

    private fun findUpcomingLunarPhaseOf(lunarPhase: LunarPhase): UpcomingLunarPhase {
        val moonPhase = MoonPhase.compute().phase(lunarPhase.toMoonPhase()).execute()
        return UpcomingLunarPhase(
            lunarPhase = lunarPhase,
            dateTime = moonPhase.time.toKotlinxLocalDateTime(),
            isMicroMoon = moonPhase.isMicroMoon,
            isSuperMoon = moonPhase.isSuperMoon
        )
    }

    companion object {
        val INITIAL_LUNAR_PHASE_DETAILS_STATE = State.Error("Has no Lunar Phase details")
        val INITIAL_UPCOMING_LUNAR_PHASE_STATE = State.Error("Has no Upcoming Lunar Phase")
    }
}
