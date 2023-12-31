package dev.mslalith.focuslauncher.feature.lunarcalendar.bottomsheet.lunarphasedetails.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import dev.mslalith.focuslauncher.feature.lunarcalendar.R
import kotlinx.datetime.LocalDateTime

@Composable
internal fun RiseTimeDetails(
    moonRiseDateTime: LocalDateTime?,
    sunRiseDateTime: LocalDateTime?,
    contentColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        RiseAndSetTime(
            localDateTime = moonRiseDateTime,
            contentColor = contentColor
        )
        RiseAndSetIndicator(
            text = stringResource(id = R.string.rise),
            contentColor = contentColor
        )
        RiseAndSetTime(
            localDateTime = sunRiseDateTime,
            contentColor = contentColor
        )
    }
}
