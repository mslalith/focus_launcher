package dev.mslalith.focuslauncher.feature.lunarcalendar.bottomsheet.lunarphasedetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.data.repository.LunarPhaseDetailsRepo
import dev.mslalith.focuslauncher.core.screens.LunarPhaseDetailsBottomSheetScreen
import javax.inject.Inject

@CircuitInject(LunarPhaseDetailsBottomSheetScreen::class, SingletonComponent::class)
class LunarPhaseDetailsBottomSheetPresenter @Inject constructor(
    private val lunarPhaseDetailsRepo: LunarPhaseDetailsRepo
) : Presenter<LunarPhaseDetailsBottomSheetState> {

    @Composable
    override fun present(): LunarPhaseDetailsBottomSheetState {
        val lunarPhaseDetails by lunarPhaseDetailsRepo.lunarPhaseDetailsStateFlow.collectAsRetainedState()

        return LunarPhaseDetailsBottomSheetState(
            lunarPhaseDetails = lunarPhaseDetails
        )
    }
}
