package dev.mslalith.focuslauncher.core.data.test.repository

import dev.mslalith.focuslauncher.core.common.model.State
import dev.mslalith.focuslauncher.core.data.repository.LunarPhaseDetailsRepo
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhaseDetails
import dev.mslalith.focuslauncher.core.model.lunarphase.UpcomingLunarPhase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Instant

class FakeLunarPhaseDetailsRepo : LunarPhaseDetailsRepo {

    private val _lunarPhaseDetailsStateFlow = MutableStateFlow<State<LunarPhaseDetails>>(value = State.Initial)
    override val lunarPhaseDetailsStateFlow: StateFlow<State<LunarPhaseDetails>> = _lunarPhaseDetailsStateFlow

    private val _upcomingLunarPhaseStateFlow = MutableStateFlow<State<UpcomingLunarPhase>>(value = State.Initial)
    override val upcomingLunarPhaseStateFlow: StateFlow<State<UpcomingLunarPhase>> = _upcomingLunarPhaseStateFlow

    override suspend fun refreshLunarPhaseDetails(instant: Instant, latLng: LatLng) = Unit

    fun setLunarPhaseDetails(details: LunarPhaseDetails) {
        _lunarPhaseDetailsStateFlow.update { State.Success(value = details) }
    }

    fun setUpcomingLunarPhase(details: UpcomingLunarPhase) {
        _upcomingLunarPhaseStateFlow.update { State.Success(value = details) }
    }
}
