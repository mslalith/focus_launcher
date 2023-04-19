package dev.mslalith.focuslauncher.feature.lunarcalendar.detailsdialog

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import kotlinx.datetime.LocalDateTime

@Composable
internal fun RiseTimeDetails(
    moonRiseDateTime: LocalDateTime?,
    sunRiseDateTime: LocalDateTime?,
    contentColor: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RiseAndSetTime(
            localDateTime = moonRiseDateTime,
            contentColor = contentColor
        )
        RiseAndSetIndicator(
            text = "Rise",
            contentColor = contentColor
        )
        RiseAndSetTime(
            localDateTime = sunRiseDateTime,
            contentColor = contentColor
        )
    }
}
