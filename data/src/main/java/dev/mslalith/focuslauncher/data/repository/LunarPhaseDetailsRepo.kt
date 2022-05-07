package dev.mslalith.focuslauncher.data.repository

import androidx.annotation.VisibleForTesting
import dev.mslalith.focuslauncher.data.model.LunarPhase
import dev.mslalith.focuslauncher.data.model.LunarPhaseDetails
import dev.mslalith.focuslauncher.data.model.LunarPhaseDirection
import dev.mslalith.focuslauncher.data.model.State
import dev.mslalith.focuslauncher.data.model.UpcomingLunarPhase
import dev.mslalith.focuslauncher.data.model.toLunarPhase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaInstant
import org.shredzone.commons.suncalc.MoonIllumination
import org.shredzone.commons.suncalc.MoonPhase
import javax.inject.Inject
import kotlin.random.Random

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

    suspend fun refreshLunarPhaseDetails(instant: Instant) {
        val delayInMillis = Random.nextInt(from = 8, until = 17) * 100L
        delay(delayInMillis)

        val lunarPhaseDetails = findLunarPhaseDetails(instant)
        updateStateFlowsWith(lunarPhaseDetails)
    }

    private fun updateStateFlowsWith(lunarPhaseDetails: LunarPhaseDetails) {
        _lunarPhaseDetailsStateFlow.value = State.Success(lunarPhaseDetails)
        _upcomingLunarPhaseStateFlow.value =
            State.Success(findUpcomingMoonPhaseFor(lunarPhaseDetails.direction))
    }

    @VisibleForTesting
    fun findLunarPhaseDetails(instant: Instant): LunarPhaseDetails =
        MoonIllumination.compute().on(instant.toJavaInstant()).execute().run {
            LunarPhaseDetails(
                lunarPhase = closestPhase.toLunarPhase(),
                illumination = fraction,
                phaseAngle = phase,
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

    companion object {
        val INITIAL_LUNAR_PHASE_DETAILS_STATE = State.Error("Has no Lunar Phase details")
        val INITIAL_UPCOMING_LUNAR_PHASE_STATE = State.Error("Has no Upcoming Lunar Phase")
    }
}
