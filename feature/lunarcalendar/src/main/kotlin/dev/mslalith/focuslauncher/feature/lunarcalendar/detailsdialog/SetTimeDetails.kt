package dev.mslalith.focuslauncher.feature.lunarcalendar.detailsdialog

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import dev.mslalith.focuslauncher.feature.lunarcalendar.R
import kotlinx.datetime.LocalDateTime

@Composable
internal fun SetTimeDetails(
    moonSetDateTime: LocalDateTime?,
    sunSetDateTime: LocalDateTime?,
    contentColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        RiseAndSetTime(
            localDateTime = moonSetDateTime,
            contentColor = contentColor
        )
        RiseAndSetIndicator(
            text = stringResource(id = R.string.set),
            contentColor = contentColor
        )
        RiseAndSetTime(
            localDateTime = sunSetDateTime,
            contentColor = contentColor
        )
    }
}
