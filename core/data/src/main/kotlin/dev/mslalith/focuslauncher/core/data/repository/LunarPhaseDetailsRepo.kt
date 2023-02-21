package dev.mslalith.focuslauncher.core.data.repository

import dev.mslalith.focuslauncher.core.common.State
import dev.mslalith.focuslauncher.core.model.City
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhaseDetails
import dev.mslalith.focuslauncher.core.model.lunarphase.UpcomingLunarPhase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Instant

interface LunarPhaseDetailsRepo {
    val lunarPhaseDetailsStateFlow: StateFlow<State<LunarPhaseDetails>>
    val upcomingLunarPhaseStateFlow: StateFlow<State<UpcomingLunarPhase>>

    suspend fun refreshLunarPhaseDetails(instant: Instant, city: City)
}
