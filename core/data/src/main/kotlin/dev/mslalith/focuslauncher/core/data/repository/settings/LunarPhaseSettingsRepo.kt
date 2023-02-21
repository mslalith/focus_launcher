package dev.mslalith.focuslauncher.core.data.repository.settings

import dev.mslalith.focuslauncher.core.model.City
import kotlinx.coroutines.flow.Flow

interface LunarPhaseSettingsRepo {
    val showLunarPhaseFlow: Flow<Boolean>
    val showIlluminationPercentFlow: Flow<Boolean>
    val showUpcomingPhaseDetailsFlow: Flow<Boolean>
    val currentPlaceFlow: Flow<City>

    suspend fun toggleShowLunarPhase()
    suspend fun toggleShowIlluminationPercent()
    suspend fun toggleShowUpcomingPhaseDetails()
    suspend fun updatePlace(city: City)
}
