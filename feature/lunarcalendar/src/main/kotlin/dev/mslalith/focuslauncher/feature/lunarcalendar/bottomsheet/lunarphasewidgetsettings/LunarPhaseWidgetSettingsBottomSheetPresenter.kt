package dev.mslalith.focuslauncher.feature.lunarcalendar.bottomsheet.lunarphasewidgetsettings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.ClockRepo
import dev.mslalith.focuslauncher.core.data.repository.LunarPhaseDetailsRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.LunarPhaseSettingsRepo
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.LunarPhase.DEFAULT_CURRENT_PLACE
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_ILLUMINATION_PERCENT
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_LUNAR_PHASE
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_UPCOMING_PHASE_DETAILS
import dev.mslalith.focuslauncher.core.screens.LunarPhaseWidgetSettingsBottomSheetScreen
import dev.mslalith.focuslauncher.feature.lunarcalendar.bottomsheet.lunarphasewidgetsettings.LunarPhaseWidgetSettingsBottomSheetUiEvent.ToggleShowIlluminationPercent
import dev.mslalith.focuslauncher.feature.lunarcalendar.bottomsheet.lunarphasewidgetsettings.LunarPhaseWidgetSettingsBottomSheetUiEvent.ToggleShowLunarPhase
import dev.mslalith.focuslauncher.feature.lunarcalendar.bottomsheet.lunarphasewidgetsettings.LunarPhaseWidgetSettingsBottomSheetUiEvent.ToggleShowUpcomingPhaseDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class LunarPhaseWidgetSettingsBottomSheetPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val clockRepo: ClockRepo,
    private val lunarPhaseDetailsRepo: LunarPhaseDetailsRepo,
    private val lunarPhaseSettingsRepo: LunarPhaseSettingsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : Presenter<LunarPhaseWidgetSettingsBottomSheetState> {

    @CircuitInject(LunarPhaseWidgetSettingsBottomSheetScreen::class, SingletonComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): LunarPhaseWidgetSettingsBottomSheetPresenter
    }

    @Composable
    override fun present(): LunarPhaseWidgetSettingsBottomSheetState {
        val scope = rememberCoroutineScope()

        val showLunarPhase by lunarPhaseSettingsRepo.showLunarPhaseFlow.collectAsState(initial = DEFAULT_SHOW_LUNAR_PHASE)
        val showIlluminationPercent by lunarPhaseSettingsRepo.showIlluminationPercentFlow.collectAsState(initial = DEFAULT_SHOW_ILLUMINATION_PERCENT)
        val showUpcomingPhaseDetails by lunarPhaseSettingsRepo.showUpcomingPhaseDetailsFlow.collectAsState(initial = DEFAULT_SHOW_UPCOMING_PHASE_DETAILS)
        val lunarPhaseDetails by lunarPhaseDetailsRepo.lunarPhaseDetailsStateFlow.collectAsState()
        val upcomingLunarPhase by lunarPhaseDetailsRepo.upcomingLunarPhaseStateFlow.collectAsState()
        val currentPlace by lunarPhaseSettingsRepo.currentPlaceFlow.collectAsState(initial = DEFAULT_CURRENT_PLACE)

        LaunchedEffect(key1 = Unit) {
            combine(
                clockRepo.currentInstantStateFlow,
                lunarPhaseSettingsRepo.currentPlaceFlow
            ) { instant, currentPlace ->
                lunarPhaseDetailsRepo.refreshLunarPhaseDetails(instant = instant, latLng = currentPlace.latLng)
            }.collect()
        }

        return LunarPhaseWidgetSettingsBottomSheetState(
            showLunarPhase = showLunarPhase,
            showIlluminationPercent = showIlluminationPercent,
            showUpcomingPhaseDetails = showUpcomingPhaseDetails,
            lunarPhaseDetails = lunarPhaseDetails,
            upcomingLunarPhase = upcomingLunarPhase,
            currentPlace = currentPlace
        ) {
            when (it) {
                is LunarPhaseWidgetSettingsBottomSheetUiEvent.Goto -> {
                    navigator.pop()
                    navigator.goTo(screen = it.screen)
                }
                ToggleShowLunarPhase -> scope.toggleShowLunarPhase()
                ToggleShowIlluminationPercent -> scope.toggleShowIlluminationPercent()
                ToggleShowUpcomingPhaseDetails -> scope.toggleShowUpcomingPhaseDetails()
            }
        }
    }

    private fun CoroutineScope.toggleShowLunarPhase() {
        launch(appCoroutineDispatcher.io) {
            lunarPhaseSettingsRepo.toggleShowLunarPhase()
        }
    }

    private fun CoroutineScope.toggleShowIlluminationPercent() {
        launch(appCoroutineDispatcher.io) {
            lunarPhaseSettingsRepo.toggleShowIlluminationPercent()
        }
    }

    private fun CoroutineScope.toggleShowUpcomingPhaseDetails() {
        launch(appCoroutineDispatcher.io) {
            lunarPhaseSettingsRepo.toggleShowUpcomingPhaseDetails()
        }
    }
}
