package dev.mslalith.focuslauncher.data.respository

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import dev.mslalith.focuslauncher.data.models.LunarPhase
import dev.mslalith.focuslauncher.data.models.LunarPhase.FULL_MOON
import dev.mslalith.focuslauncher.data.models.LunarPhase.NEW_MOON
import dev.mslalith.focuslauncher.data.models.LunarPhaseDetails
import dev.mslalith.focuslauncher.data.models.LunarPhaseDirection
import dev.mslalith.focuslauncher.data.models.LunarPhaseDirection.FULL_TO_NEW
import dev.mslalith.focuslauncher.data.models.LunarPhaseDirection.NEW_TO_FULL
import dev.mslalith.focuslauncher.data.models.Outcome
import dev.mslalith.focuslauncher.data.models.UpcomingLunarPhase
import dev.mslalith.focuslauncher.data.models.toLunarPhase
import dev.mslalith.focuslauncher.extensions.formatToTime
import dev.mslalith.focuslauncher.extensions.runAfter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaInstant
import org.shredzone.commons.suncalc.MoonIllumination
import org.shredzone.commons.suncalc.MoonPhase
import javax.inject.Inject
import kotlin.random.Random

class LunarPhaseDetailsRepo @Inject constructor() {
    private val _isTimeChangeBroadcastReceiverRegisteredFlow = MutableStateFlow(value = false)
    private val _currentTimeStateFlow = MutableStateFlow<Outcome<String>>(INITIAL_TIME_OUTCOME)
    private val _lunarPhaseDetailsStateFlow = MutableStateFlow<Outcome<LunarPhaseDetails>>(INITIAL_LUNAR_PHASE_DETAILS_OUTCOME)
    private val _upcomingLunarPhaseStateFlow = MutableStateFlow<Outcome<UpcomingLunarPhase>>(INITIAL_UPCOMING_LUNAR_PHASE_OUTCOME)

    val isTimeChangeBroadcastReceiverRegisteredStateFlow = _isTimeChangeBroadcastReceiverRegisteredFlow.asStateFlow()
    val currentTimeStateFlow = _currentTimeStateFlow.asStateFlow()
    val lunarPhaseDetailsStateFlow = _lunarPhaseDetailsStateFlow.asStateFlow()
    val upcomingLunarPhaseStateFlow = _upcomingLunarPhaseStateFlow.asStateFlow()

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

    fun registerToTimeChange(context: Context) {
        _isTimeChangeBroadcastReceiverRegisteredFlow.let {
            if (!it.value) {
                _isTimeChangeBroadcastReceiverRegisteredFlow.value = true
                context.registerReceiver(
                    timeChangeBroadcastReceiver,
                    IntentFilter(Intent.ACTION_TIME_TICK)
                )
            }
        }
    }

    fun unregisterToTimeChange(context: Context) {
        _isTimeChangeBroadcastReceiverRegisteredFlow.let {
            if (it.value) {
                it.value = false
                context.unregisterReceiver(timeChangeBroadcastReceiver)
            }
        }
    }

    private fun updateStateFlowsWith(lunarPhaseDetails: LunarPhaseDetails) {
        _lunarPhaseDetailsStateFlow.value = Outcome.Success(lunarPhaseDetails)
        _upcomingLunarPhaseStateFlow.value = Outcome.Success(findUpcomingMoonPhaseFor(lunarPhaseDetails.direction))
    }

    private fun findUpcomingMoonPhaseFor(lunarPhaseDirection: LunarPhaseDirection) =
        when (lunarPhaseDirection) {
            NEW_TO_FULL -> findUpcomingLunarPhaseOf(FULL_MOON)
            FULL_TO_NEW -> findUpcomingLunarPhaseOf(NEW_MOON)
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

    private fun findLunarPhaseDetails(instant: Instant): LunarPhaseDetails =
        MoonIllumination.compute().on(instant.toJavaInstant()).execute().run {
            LunarPhaseDetails(
                lunarPhase = closestPhase.toLunarPhase(),
                illumination = fraction,
                phaseAngle = phase,
            )
        }

    companion object {
        val INITIAL_TIME_OUTCOME = Outcome.Error("")
        val INITIAL_LUNAR_PHASE_DETAILS_OUTCOME = Outcome.Error("Has no Lunar Phase details")
        val INITIAL_UPCOMING_LUNAR_PHASE_OUTCOME = Outcome.Error("Has no Upcoming Lunar Phase")
    }
}
