package dev.mslalith.focuslauncher.feature.lunarcalendar.detailsdialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    val lunarPhaseDetailsState by lunarCalendarViewModel.lunarCalendarState.collectAsStateWithLifecycle()
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
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onClose: () -> Unit,
) {
    Dialog(
        onDismissRequest = onClose
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(size = 12.dp))
                .background(color = backgroundColor)
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            TodayLunarPhase(
                lunarPhaseDetails = lunarPhaseDetails,
                contentColor = contentColor
            )

            Divider(
                modifier = Modifier.padding(vertical = 16.dp)
            )

            NextMajorPhaseDetails(
                nextPhaseDetails = lunarPhaseDetails.nextPhaseDetails,
                contentColor = contentColor
            )

            Divider(
                modifier = Modifier.padding(vertical = 16.dp)
            )

            LunarRiseAndSetDetails(
                lunarPhaseDetails = lunarPhaseDetails,
                contentColor = contentColor
            )
        }
    }
}
