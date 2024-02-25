package dev.mslalith.focuslauncher.feature.lunarcalendar.widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.data.repository.ClockRepo
import dev.mslalith.focuslauncher.core.data.repository.LunarPhaseDetailsRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.LunarPhaseSettingsRepo
import dev.mslalith.focuslauncher.core.model.Constants
import dev.mslalith.focuslauncher.core.screens.LunarCalendarUiComponentScreen
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@CircuitInject(LunarCalendarUiComponentScreen::class, SingletonComponent::class)
class LunarCalendarUiComponentPresenter @Inject constructor(
    private val clockRepo: ClockRepo,
    private val lunarPhaseDetailsRepo: LunarPhaseDetailsRepo,
    private val lunarPhaseSettingsRepo: LunarPhaseSettingsRepo
) : Presenter<LunarCalendarUiComponentState> {

    @Composable
    override fun present(): LunarCalendarUiComponentState {
        val showLunarPhase by lunarPhaseSettingsRepo.showLunarPhaseFlow.collectAsRetainedState(initial = Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_LUNAR_PHASE)
        val showIlluminationPercent by lunarPhaseSettingsRepo.showIlluminationPercentFlow.collectAsRetainedState(initial = Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_ILLUMINATION_PERCENT)
        val showUpcomingPhaseDetails by lunarPhaseSettingsRepo.showUpcomingPhaseDetailsFlow.collectAsRetainedState(initial = Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_UPCOMING_PHASE_DETAILS)
        val lunarPhaseDetails by lunarPhaseDetailsRepo.lunarPhaseDetailsStateFlow.collectAsRetainedState()
        val upcomingLunarPhase by lunarPhaseDetailsRepo.upcomingLunarPhaseStateFlow.collectAsRetainedState()

        LaunchedEffect(key1 = Unit) {
            combine(
                clockRepo.currentInstantStateFlow,
                lunarPhaseSettingsRepo.currentPlaceFlow
            ) { instant, currentPlace ->
                lunarPhaseDetailsRepo.refreshLunarPhaseDetails(instant = instant, latLng = currentPlace.latLng)
            }.collect()
        }

        return LunarCalendarUiComponentState(
            showLunarPhase = showLunarPhase,
            showIlluminationPercent = showIlluminationPercent,
            showUpcomingPhaseDetails = showUpcomingPhaseDetails,
            lunarPhaseDetails = lunarPhaseDetails,
            upcomingLunarPhase = upcomingLunarPhase
        )
    }
}
