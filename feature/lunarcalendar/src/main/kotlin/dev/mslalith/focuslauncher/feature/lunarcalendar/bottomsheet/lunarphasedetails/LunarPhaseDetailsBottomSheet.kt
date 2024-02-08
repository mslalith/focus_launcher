package dev.mslalith.focuslauncher.feature.lunarcalendar.bottomsheet.lunarphasedetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.common.model.getOrNull
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhaseDetails
import dev.mslalith.focuslauncher.core.screens.LunarPhaseDetailsBottomSheetScreen
import dev.mslalith.focuslauncher.feature.lunarcalendar.bottomsheet.lunarphasedetails.ui.LunarRiseAndSetDetails
import dev.mslalith.focuslauncher.feature.lunarcalendar.bottomsheet.lunarphasedetails.ui.NextMajorPhaseDetails
import dev.mslalith.focuslauncher.feature.lunarcalendar.bottomsheet.lunarphasedetails.ui.TodayLunarPhase

@CircuitInject(LunarPhaseDetailsBottomSheetScreen::class, SingletonComponent::class)
@Composable
fun LunarPhaseDetailsBottomSheet(
    state: LunarPhaseDetailsBottomSheetState,
    modifier: Modifier = Modifier
) {
    state.lunarPhaseDetails.getOrNull()?.let {
        LunarPhaseDetailsBottomSheetContent(
            modifier = modifier,
            lunarPhaseDetails = it
        )
    }
}

@Composable
private fun LunarPhaseDetailsBottomSheetContent(
    lunarPhaseDetails: LunarPhaseDetails,
    modifier: Modifier = Modifier,
    contentColor: Color = LocalContentColor.current
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(vertical = 16.dp)
    ) {
        TodayLunarPhase(
            lunarPhaseDetails = lunarPhaseDetails,
            contentColor = contentColor
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp)
        )

        NextMajorPhaseDetails(
            nextPhaseDetails = lunarPhaseDetails.nextPhaseDetails,
            contentColor = contentColor
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp)
        )

        LunarRiseAndSetDetails(
            lunarPhaseDetails = lunarPhaseDetails,
            contentColor = contentColor
        )
    }
}
