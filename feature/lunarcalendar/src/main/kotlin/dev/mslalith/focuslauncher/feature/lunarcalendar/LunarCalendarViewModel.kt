package dev.mslalith.focuslauncher.feature.lunarcalendar

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.common.State
import dev.mslalith.focuslauncher.core.ui.extensions.launchInIO
import dev.mslalith.focuslauncher.core.ui.extensions.withinScope
import dev.mslalith.focuslauncher.data.repository.ClockRepo
import dev.mslalith.focuslauncher.data.repository.LunarPhaseDetailsRepo
import dev.mslalith.focuslauncher.data.repository.settings.LunarPhaseSettingsRepo
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_ILLUMINATION_PERCENT
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_LUNAR_PHASE
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_UPCOMING_PHASE_DETAILS
import dev.mslalith.focuslauncher.feature.lunarcalendar.model.LunarCalendarState
import javax.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class LunarCalendarViewModel @Inject constructor(
    clockRepo: ClockRepo,
    lunarPhaseDetailsRepo: LunarPhaseDetailsRepo,
    lunarPhaseSettingsRepo: LunarPhaseSettingsRepo,
    appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    init {
        appCoroutineDispatcher.launchInIO {
            clockRepo.currentInstantStateFlow.combine(lunarPhaseSettingsRepo.currentPlaceFlow) { instant, city ->
                instant to city
            }.onEach { (instant, city) ->
                lunarPhaseDetailsRepo.refreshLunarPhaseDetails(instant, city)
            }
        }
    }

    private val defaultLunarCalendarState = LunarCalendarState(
        showLunarPhase = DEFAULT_SHOW_LUNAR_PHASE,
        showIlluminationPercent = DEFAULT_SHOW_ILLUMINATION_PERCENT,
        showUpcomingPhaseDetails = DEFAULT_SHOW_UPCOMING_PHASE_DETAILS,
        lunarPhaseDetails = INITIAL_LUNAR_PHASE_DETAILS_STATE,
        upcomingLunarPhase = INITIAL_UPCOMING_LUNAR_PHASE_STATE,
    )

    val lunarCalendarState = flowOf(defaultLunarCalendarState)
        .combine(lunarPhaseSettingsRepo.showLunarPhaseFlow) { state, showLunarPhase ->
            state.copy(showLunarPhase = showLunarPhase)
        }.combine(lunarPhaseSettingsRepo.showIlluminationPercentFlow) { state, showIlluminationPercent ->
            state.copy(showIlluminationPercent = showIlluminationPercent)
        }.combine(lunarPhaseSettingsRepo.showUpcomingPhaseDetailsFlow) { state, showUpcomingPhaseDetails ->
            state.copy(showUpcomingPhaseDetails = showUpcomingPhaseDetails)
        }.combine(lunarPhaseDetailsRepo.lunarPhaseDetailsStateFlow) { state, lunarPhaseDetails ->
            state.copy(lunarPhaseDetails = lunarPhaseDetails)
        }.combine(lunarPhaseDetailsRepo.upcomingLunarPhaseStateFlow) { state, upcomingLunarPhase ->
            state.copy(upcomingLunarPhase = upcomingLunarPhase)
        }.withinScope(initialValue = defaultLunarCalendarState)

    companion object {
        val INITIAL_LUNAR_PHASE_DETAILS_STATE = State.Error("Has no Lunar Phase details")
        val INITIAL_UPCOMING_LUNAR_PHASE_STATE = State.Error("Has no Upcoming Lunar Phase")
    }
}
