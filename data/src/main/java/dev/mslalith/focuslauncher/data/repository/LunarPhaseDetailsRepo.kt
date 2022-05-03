package dev.mslalith.focuslauncher.data.repository

import androidx.annotation.VisibleForTesting
import dev.mslalith.focuslauncher.data.model.LunarPhase
import dev.mslalith.focuslauncher.data.model.LunarPhaseDetails
import dev.mslalith.focuslauncher.data.model.LunarPhaseDirection
import dev.mslalith.focuslauncher.data.model.Outcome
import dev.mslalith.focuslauncher.data.model.UpcomingLunarPhase
import dev.mslalith.focuslauncher.data.model.toLunarPhase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaInstant
import org.shredzone.commons.suncalc.MoonIllumination
import org.shredzone.commons.suncalc.MoonPhase
import javax.inject.Inject
import kotlin.random.Random

class LunarPhaseDetailsRepo @Inject constructor(
    clockRepo: ClockRepo
) {
    private val _lunarPhaseDetailsStateFlow = MutableStateFlow<Outcome<LunarPhaseDetails>>(
        INITIAL_LUNAR_PHASE_DETAILS_OUTCOME)
    val lunarPhaseDetailsStateFlow: StateFlow<Outcome<LunarPhaseDetails>>
        get() = _lunarPhaseDetailsStateFlow

    private val _upcomingLunarPhaseStateFlow = MutableStateFlow<Outcome<UpcomingLunarPhase>>(
        INITIAL_UPCOMING_LUNAR_PHASE_OUTCOME)
    val upcomingLunarPhaseStateFlow: StateFlow<Outcome<UpcomingLunarPhase>>
        get() = _upcomingLunarPhaseStateFlow

    init {
        clockRepo.currentInstantStateFlow.onEach { refreshLunarPhaseDetails(it) }
    }

    private suspend fun refreshLunarPhaseDetails(instant: Instant) {
        val delayInMillis = Random.nextInt(from = 8, until = 17) * 100L
        delay(delayInMillis)

        val lunarPhaseDetails = findLunarPhaseDetails(instant)
        updateStateFlowsWith(lunarPhaseDetails)
    }

    private fun updateStateFlowsWith(lunarPhaseDetails: LunarPhaseDetails) {
        _lunarPhaseDetailsStateFlow.value = Outcome.Success(lunarPhaseDetails)
        _upcomingLunarPhaseStateFlow.value = Outcome.Success(findUpcomingMoonPhaseFor(lunarPhaseDetails.direction))
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
        val INITIAL_LUNAR_PHASE_DETAILS_OUTCOME = Outcome.Error("Has no Lunar Phase details")
        val INITIAL_UPCOMING_LUNAR_PHASE_OUTCOME = Outcome.Error("Has no Upcoming Lunar Phase")
    }
}
