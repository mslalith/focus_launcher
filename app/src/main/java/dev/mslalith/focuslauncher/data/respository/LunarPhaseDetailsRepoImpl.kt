package dev.mslalith.focuslauncher.data.respository

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import dev.mslalith.focuslauncher.data.models.LunarPhaseDetails
import dev.mslalith.focuslauncher.data.models.Outcome
import dev.mslalith.focuslauncher.data.models.UpcomingLunarPhase
import dev.mslalith.focuslauncher.data.respository.interfaces.LunarPhaseDetailsRepo
import dev.mslalith.focuslauncher.extensions.formatToTime
import dev.mslalith.focuslauncher.extensions.runAfter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import javax.inject.Inject
import kotlin.random.Random

class LunarPhaseDetailsRepoImpl @Inject constructor() : LunarPhaseDetailsRepo() {
    private val _currentTimeStateFlow = MutableStateFlow<Outcome<String>>(INITIAL_TIME_OUTCOME)
    override val currentTimeStateFlow: StateFlow<Outcome<String>>
        get() = _currentTimeStateFlow

    private val _lunarPhaseDetailsStateFlow = MutableStateFlow<Outcome<LunarPhaseDetails>>(INITIAL_LUNAR_PHASE_DETAILS_OUTCOME)
    override val lunarPhaseDetailsStateFlow: StateFlow<Outcome<LunarPhaseDetails>>
        get() = _lunarPhaseDetailsStateFlow

    private val _upcomingLunarPhaseStateFlow = MutableStateFlow<Outcome<UpcomingLunarPhase>>(INITIAL_UPCOMING_LUNAR_PHASE_OUTCOME)
    override val upcomingLunarPhaseStateFlow: StateFlow<Outcome<UpcomingLunarPhase>>
        get() = _upcomingLunarPhaseStateFlow

    init {
        val currentInstant = Clock.System.now()
        val lunarPhaseDetails = findLunarPhaseDetails(currentInstant)

        _currentTimeStateFlow.value = Outcome.Success(currentInstant.formatToTime())
        updateStateFlowsWith(lunarPhaseDetails)
    }

    private val timeChangeBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val currentInstant = Clock.System.now()
            val delayInMillis = Random.nextInt(from = 8, until = 17) * 100L
            _currentTimeStateFlow.value = Outcome.Success(currentInstant.formatToTime())

            runAfter(delayInMillis) {
                val lunarPhaseDetails = findLunarPhaseDetails(currentInstant)
                updateStateFlowsWith(lunarPhaseDetails)
            }
        }
    }

    override fun registerToTimeChange(context: Context) {
        context.registerReceiver(
            timeChangeBroadcastReceiver,
            IntentFilter(Intent.ACTION_TIME_TICK)
        )
    }

    override fun unregisterToTimeChange(context: Context) {
        try {
            context.unregisterReceiver(timeChangeBroadcastReceiver)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }
    }

    private fun updateStateFlowsWith(lunarPhaseDetails: LunarPhaseDetails) {
        _lunarPhaseDetailsStateFlow.value = Outcome.Success(lunarPhaseDetails)
        _upcomingLunarPhaseStateFlow.value = Outcome.Success(findUpcomingMoonPhaseFor(lunarPhaseDetails.direction))
    }

    companion object {
        val INITIAL_TIME_OUTCOME = Outcome.Error("")
        val INITIAL_LUNAR_PHASE_DETAILS_OUTCOME = Outcome.Error("Has no Lunar Phase details")
        val INITIAL_UPCOMING_LUNAR_PHASE_OUTCOME = Outcome.Error("Has no Upcoming Lunar Phase")
    }
}
