package dev.mslalith.focuslauncher.feature.lunarcalendar.detailsdialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.model.lunarphase.NextPhaseDetails
import dev.mslalith.focuslauncher.core.ui.HorizontalSpacer
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer

@Composable
internal fun NextMajorPhaseDetails(
    nextPhaseDetails: NextPhaseDetails,
    contentColor: Color
) {
    Column {
        Text(
            text = "Upcoming Phases",
            color = contentColor,
            style = MaterialTheme.typography.titleMedium
        )

        VerticalSpacer(spacing = 12.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(intrinsicSize = IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier.weight(weight = 1f)
            ) {
                NextSingleMajorPhaseDetails(
                    illumination = 0.0,
                    localDateTime = nextPhaseDetails.newMoon,
                    contentColor = contentColor
                )
            }

            HorizontalSpacer(spacing = 12.dp)

            Box(
                modifier = Modifier.weight(weight = 1f)
            ) {
                NextSingleMajorPhaseDetails(
                    illumination = 100.0,
                    localDateTime = nextPhaseDetails.fullMoon,
                    contentColor = contentColor
                )
            }
        }
    }
}
