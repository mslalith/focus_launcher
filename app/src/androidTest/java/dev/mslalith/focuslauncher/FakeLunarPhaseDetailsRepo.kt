package dev.mslalith.focuslauncher

import dev.mslalith.focuslauncher.data.models.LunarPhaseDetails
import dev.mslalith.focuslauncher.data.models.Outcome
import dev.mslalith.focuslauncher.data.models.UpcomingLunarPhase
import dev.mslalith.focuslauncher.data.repository.interfaces.LunarPhaseDetailsRepo
import dev.mslalith.focuslauncher.extensions.formatToTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant

class FakeLunarPhaseDetailsRepo(
    private val coroutineScope: CoroutineScope,
) : LunarPhaseDetailsRepo() {
    private val _isTimeChangeBroadcastReceiverRegisteredFlow = MutableStateFlow(value = false)
    val isTimeChangeBroadcastReceiverRegisteredStateFlow: StateFlow<Boolean>
        get() = _isTimeChangeBroadcastReceiverRegisteredFlow

    private val _currentTimeStateFlow = MutableStateFlow<Outcome<String>>(Outcome.Error(""))
    override val currentTimeStateFlow: StateFlow<Outcome<String>>
        get() = _currentTimeStateFlow

    private val _lunarPhaseDetailsStateFlow = MutableStateFlow<Outcome<LunarPhaseDetails>>(Outcome.Error(""))
    override val lunarPhaseDetailsStateFlow: StateFlow<Outcome<LunarPhaseDetails>>
        get() = _lunarPhaseDetailsStateFlow

    private val _upcomingLunarPhaseStateFlow = MutableStateFlow<Outcome<UpcomingLunarPhase>>(Outcome.Error(""))
    override val upcomingLunarPhaseStateFlow: StateFlow<Outcome<UpcomingLunarPhase>>
        get() = _upcomingLunarPhaseStateFlow

    private var tickJob: Job? = null

    var instantTimeList = listOf<Instant>()

    private fun setupTickJob() {
        tickJob = coroutineScope.launch {
            instantTimeList.forEach { currentInstant ->
                _currentTimeStateFlow.value = Outcome.Success(currentInstant.formatToTime())

                val lunarPhaseDetails = findLunarPhaseDetails(currentInstant)
                updateStateFlowsWith(lunarPhaseDetails)
            }
            instantTimeList = emptyList()
            unregisterToTimeChange()
        }
    }

    private fun updateStateFlowsWith(lunarPhaseDetails: LunarPhaseDetails) {
        _lunarPhaseDetailsStateFlow.value = Outcome.Success(lunarPhaseDetails)
        _upcomingLunarPhaseStateFlow.value = Outcome.Success(findUpcomingMoonPhaseFor(lunarPhaseDetails.direction))
    }

    override fun refreshTime() {
        TODO("Not yet implemented")
    }

    fun registerToTimeChange() {
        _isTimeChangeBroadcastReceiverRegisteredFlow.value = true
        setupTickJob()
    }

    fun unregisterToTimeChange() {
        _isTimeChangeBroadcastReceiverRegisteredFlow.value = false
        tickJob?.cancel()
        tickJob = null
    }
}
