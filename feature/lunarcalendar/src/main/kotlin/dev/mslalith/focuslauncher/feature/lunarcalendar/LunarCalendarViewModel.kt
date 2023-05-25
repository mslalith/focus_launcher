package dev.mslalith.focuslauncher.feature.lunarcalendar

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.model.State
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.ClockRepo
import dev.mslalith.focuslauncher.core.data.repository.LunarPhaseDetailsRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.LunarPhaseSettingsRepo
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.LunarPhase.DEFAULT_CURRENT_PLACE
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_ILLUMINATION_PERCENT
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_LUNAR_PHASE
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_UPCOMING_PHASE_DETAILS
import dev.mslalith.focuslauncher.core.ui.extensions.launchInIO
import dev.mslalith.focuslauncher.core.ui.extensions.withinScope
import dev.mslalith.focuslauncher.feature.lunarcalendar.model.LunarCalendarState
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf

@HiltViewModel
internal class LunarCalendarViewModel @Inject constructor(
    clockRepo: ClockRepo,
    lunarPhaseDetailsRepo: LunarPhaseDetailsRepo,
    private val lunarPhaseSettingsRepo: LunarPhaseSettingsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    init {
        appCoroutineDispatcher.launchInIO {
            clockRepo.currentInstantStateFlow
                .combine(flow = lunarPhaseSettingsRepo.currentPlaceFlow) { instant, currentPlace ->
                    instant to currentPlace
                }.collectLatest { (instant, currentPlace) ->
                    lunarPhaseDetailsRepo.refreshLunarPhaseDetails(instant = instant, latLng = currentPlace.latLng)
                }
        }
    }

    private val defaultLunarCalendarState = LunarCalendarState(
        showLunarPhase = DEFAULT_SHOW_LUNAR_PHASE,
        showIlluminationPercent = DEFAULT_SHOW_ILLUMINATION_PERCENT,
        showUpcomingPhaseDetails = DEFAULT_SHOW_UPCOMING_PHASE_DETAILS,
        lunarPhaseDetails = State.Initial,
        upcomingLunarPhase = State.Initial
    )

    val lunarCalendarState = flowOf(value = defaultLunarCalendarState)
        .combine(flow = lunarPhaseSettingsRepo.showLunarPhaseFlow) { state, showLunarPhase ->
            state.copy(showLunarPhase = showLunarPhase)
        }.combine(flow = lunarPhaseSettingsRepo.showIlluminationPercentFlow) { state, showIlluminationPercent ->
            state.copy(showIlluminationPercent = showIlluminationPercent)
        }.combine(flow = lunarPhaseSettingsRepo.showUpcomingPhaseDetailsFlow) { state, showUpcomingPhaseDetails ->
            state.copy(showUpcomingPhaseDetails = showUpcomingPhaseDetails)
        }.combine(flow = lunarPhaseDetailsRepo.lunarPhaseDetailsStateFlow) { state, lunarPhaseDetails ->
            state.copy(lunarPhaseDetails = lunarPhaseDetails)
        }.combine(flow = lunarPhaseDetailsRepo.upcomingLunarPhaseStateFlow) { state, upcomingLunarPhase ->
            state.copy(upcomingLunarPhase = upcomingLunarPhase)
        }.withinScope(initialValue = defaultLunarCalendarState)

    val currentPlaceStateFlow = lunarPhaseSettingsRepo.currentPlaceFlow.withinScope(initialValue = DEFAULT_CURRENT_PLACE)

    fun toggleShowLunarPhase() {
        appCoroutineDispatcher.launchInIO {
            lunarPhaseSettingsRepo.toggleShowLunarPhase()
        }
    }

    fun toggleShowIlluminationPercent() {
        appCoroutineDispatcher.launchInIO {
            lunarPhaseSettingsRepo.toggleShowIlluminationPercent()
        }
    }

    fun toggleShowUpcomingPhaseDetails() {
        appCoroutineDispatcher.launchInIO {
            lunarPhaseSettingsRepo.toggleShowUpcomingPhaseDetails()
        }
    }
}
