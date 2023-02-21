package dev.mslalith.focuslauncher.feature.lunarcalendar.detailsdialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import dev.mslalith.focuslauncher.core.common.getOrNull
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhaseDetails
import dev.mslalith.focuslauncher.feature.lunarcalendar.LunarCalendarViewModel

@Composable
fun LunarPhaseDetailsDialog(
    onClose: () -> Unit,
) {
    LunarPhaseDetailsDialog(
        lunarCalendarViewModel = hiltViewModel(),
        onClose = onClose
    )
}

@Composable
internal fun LunarPhaseDetailsDialog(
    lunarCalendarViewModel: LunarCalendarViewModel,
    onClose: () -> Unit,
) {
    val lunarPhaseDetailsState by lunarCalendarViewModel.lunarCalendarState.collectAsState()
    lunarPhaseDetailsState.lunarPhaseDetails.getOrNull()?.let { phaseDetails ->
        LunarPhaseDetailsDialog(
            lunarPhaseDetails = phaseDetails,
            onClose = onClose
        )
    }
}

@Composable
internal fun LunarPhaseDetailsDialog(
    lunarPhaseDetails: LunarPhaseDetails,
    onClose: () -> Unit,
) {
    Dialog(
        onDismissRequest = onClose
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(size = 12.dp))
                .background(color = MaterialTheme.colors.primaryVariant)
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            TodayLunarPhase(lunarPhaseDetails = lunarPhaseDetails)

            Divider(
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.1f),
                modifier = Modifier.padding(vertical = 16.dp)
            )

            NextMajorPhaseDetails(lunarPhaseDetails.nextPhaseDetails)

            Divider(
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.1f),
                modifier = Modifier.padding(vertical = 16.dp)
            )

            LunarRiseAndSetDetails(lunarPhaseDetails = lunarPhaseDetails)
        }
    }
}
