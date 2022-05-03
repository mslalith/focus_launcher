package dev.mslalith.focuslauncher.data.repository

import dev.mslalith.focuslauncher.data.models.LunarPhaseDetails
import dev.mslalith.focuslauncher.data.models.Outcome
import dev.mslalith.focuslauncher.data.models.UpcomingLunarPhase
import dev.mslalith.focuslauncher.data.repository.interfaces.LunarPhaseDetailsRepo
import dev.mslalith.focuslauncher.extensions.formatToTime
import dev.mslalith.focuslauncher.extensions.runAfter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import javax.inject.Inject
import kotlin.random.Random

class LunarPhaseDetailsRepoImpl @Inject constructor(
    clockRepo: ClockRepo
) : LunarPhaseDetailsRepo() {
    private val _currentTimeStateFlow = MutableStateFlow<Outcome<String>>(ClockRepo.INITIAL_TIME_OUTCOME)
    override val currentTimeStateFlow: StateFlow<Outcome<String>>
        get() = _currentTimeStateFlow

    private val _lunarPhaseDetailsStateFlow = MutableStateFlow<Outcome<LunarPhaseDetails>>(INITIAL_LUNAR_PHASE_DETAILS_OUTCOME)
    override val lunarPhaseDetailsStateFlow: StateFlow<Outcome<LunarPhaseDetails>>
        get() = _lunarPhaseDetailsStateFlow

    private val _upcomingLunarPhaseStateFlow = MutableStateFlow<Outcome<UpcomingLunarPhase>>(INITIAL_UPCOMING_LUNAR_PHASE_OUTCOME)
    override val upcomingLunarPhaseStateFlow: StateFlow<Outcome<UpcomingLunarPhase>>
        get() = _upcomingLunarPhaseStateFlow

    init {
        clockRepo.currentInstantStateFlow.onEach { refreshLunarPhaseDetails(it) }
        val currentInstant = Clock.System.now()
        val lunarPhaseDetails = findLunarPhaseDetails(currentInstant)

        _currentTimeStateFlow.value = Outcome.Success(currentInstant.formatToTime())
        updateStateFlowsWith(lunarPhaseDetails)
    }

    override fun refreshTime() {
        val currentInstant = Clock.System.now()
        val delayInMillis = Random.nextInt(from = 8, until = 17) * 100L
        _currentTimeStateFlow.value = Outcome.Success(currentInstant.formatToTime())

        runAfter(delayInMillis) {
            val lunarPhaseDetails = findLunarPhaseDetails(currentInstant)
            updateStateFlowsWith(lunarPhaseDetails)
        }
    }

    suspend fun refreshLunarPhaseDetails(instant: Instant) {
        val delayInMillis = Random.nextInt(from = 8, until = 17) * 100L
        delay(delayInMillis)

        val lunarPhaseDetails = findLunarPhaseDetails(instant)
        updateStateFlowsWith(lunarPhaseDetails)
    }

    private fun updateStateFlowsWith(lunarPhaseDetails: LunarPhaseDetails) {
        _lunarPhaseDetailsStateFlow.value = Outcome.Success(lunarPhaseDetails)
        _upcomingLunarPhaseStateFlow.value = Outcome.Success(findUpcomingMoonPhaseFor(lunarPhaseDetails.direction))
    }

    companion object {
        val INITIAL_LUNAR_PHASE_DETAILS_OUTCOME = Outcome.Error("Has no Lunar Phase details")
        val INITIAL_UPCOMING_LUNAR_PHASE_OUTCOME = Outcome.Error("Has no Upcoming Lunar Phase")
    }
}
