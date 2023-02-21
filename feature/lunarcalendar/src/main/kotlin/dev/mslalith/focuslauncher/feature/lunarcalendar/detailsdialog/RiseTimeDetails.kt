package dev.mslalith.focuslauncher.feature.lunarcalendar.detailsdialog

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import kotlinx.datetime.LocalDateTime

@Composable
internal fun RiseTimeDetails(
    moonRiseDateTime: LocalDateTime?,
    sunRiseDateTime: LocalDateTime?
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RiseAndSetTime(localDateTime = moonRiseDateTime)
        RiseAndSetIndicator(text = "Rise")
        RiseAndSetTime(localDateTime = sunRiseDateTime)
    }
}
