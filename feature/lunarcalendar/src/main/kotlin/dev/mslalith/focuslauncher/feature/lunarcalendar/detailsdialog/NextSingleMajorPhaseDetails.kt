package dev.mslalith.focuslauncher.feature.lunarcalendar.detailsdialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.common.extensions.inShortReadableFormat
import dev.mslalith.focuslauncher.core.ui.HorizontalSpacer
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.feature.lunarcalendar.shared.LunarPhaseMoonIcon
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaInstant

@Composable
internal fun NextSingleMajorPhaseDetails(
    illumination: Double,
    localDateTime: LocalDateTime?,
    contentColor: Color
) {
    val date = localDateTime?.inShortReadableFormat(shortMonthName = true) ?: "-"

    val time = if (localDateTime != null) {
        val javaInstant = localDateTime.toInstant(TimeZone.currentSystemDefault()).toJavaInstant()
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date.from(javaInstant))
    } else {
        "-"
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LunarPhaseMoonIcon(
            phaseAngle = 0.0,
            illumination = illumination,
            moonSize = 40.dp
        )

        HorizontalSpacer(spacing = 8.dp)

        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = date,
                color = contentColor,
                style = MaterialTheme.typography.bodyMedium
            )

            VerticalSpacer(spacing = 4.dp)

            Text(
                text = time,
                color = contentColor,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
