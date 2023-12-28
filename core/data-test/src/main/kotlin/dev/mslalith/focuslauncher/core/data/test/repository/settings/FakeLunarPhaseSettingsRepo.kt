package dev.mslalith.focuslauncher.core.data.test.repository.settings

import dev.mslalith.focuslauncher.core.data.repository.settings.LunarPhaseSettingsRepo
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.LunarPhase.DEFAULT_CURRENT_PLACE
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_ILLUMINATION_PERCENT
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_LUNAR_PHASE
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_UPCOMING_PHASE_DETAILS
import dev.mslalith.focuslauncher.core.model.CurrentPlace
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeLunarPhaseSettingsRepo : LunarPhaseSettingsRepo {

    private val showLunarPhaseStateFlow = MutableStateFlow(value = DEFAULT_SHOW_LUNAR_PHASE)
    override val showLunarPhaseFlow: Flow<Boolean> = showLunarPhaseStateFlow

    private val showIlluminationPercentStateFlow = MutableStateFlow(value = DEFAULT_SHOW_ILLUMINATION_PERCENT)
    override val showIlluminationPercentFlow: Flow<Boolean> = showIlluminationPercentStateFlow

    private val showUpcomingPhaseDetailsStateFlow = MutableStateFlow(value = DEFAULT_SHOW_UPCOMING_PHASE_DETAILS)
    override val showUpcomingPhaseDetailsFlow: Flow<Boolean> = showUpcomingPhaseDetailsStateFlow

    private val currentPlaceStateFlow = MutableStateFlow(value = DEFAULT_CURRENT_PLACE)
    override val currentPlaceFlow: Flow<CurrentPlace> = currentPlaceStateFlow

    override suspend fun toggleShowLunarPhase() {
        showLunarPhaseStateFlow.update { !it }
    }

    override suspend fun toggleShowIlluminationPercent() {
        showIlluminationPercentStateFlow.update { !it }
    }

    override suspend fun toggleShowUpcomingPhaseDetails() {
        showUpcomingPhaseDetailsStateFlow.update { !it }
    }

    override suspend fun updateCurrentPlace(currentPlace: CurrentPlace) {
        currentPlaceStateFlow.update { currentPlace }
    }
}
