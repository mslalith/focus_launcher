package dev.mslalith.focuslauncher.features.lunarcalendar.detailsdialog

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import kotlinx.datetime.LocalDateTime

@Composable
internal fun SetTimeDetails(
    moonSetDateTime: LocalDateTime?,
    sunSetDateTime: LocalDateTime?
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RiseAndSetTime(localDateTime = moonSetDateTime)
        RiseAndSetIndicator(text = "Set")
        RiseAndSetTime(localDateTime = sunSetDateTime)
    }
}
