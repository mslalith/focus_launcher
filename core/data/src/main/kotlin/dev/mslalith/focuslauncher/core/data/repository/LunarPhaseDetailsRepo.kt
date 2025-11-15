package dev.mslalith.focuslauncher.core.data.repository

import dev.mslalith.focuslauncher.core.common.model.State
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhaseDetails
import dev.mslalith.focuslauncher.core.model.lunarphase.UpcomingLunarPhase
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

interface LunarPhaseDetailsRepo {
    val lunarPhaseDetailsStateFlow: StateFlow<State<LunarPhaseDetails>>
    val upcomingLunarPhaseStateFlow: StateFlow<State<UpcomingLunarPhase>>

    @OptIn(ExperimentalTime::class)
    suspend fun refreshLunarPhaseDetails(instant: Instant, latLng: LatLng)
}
