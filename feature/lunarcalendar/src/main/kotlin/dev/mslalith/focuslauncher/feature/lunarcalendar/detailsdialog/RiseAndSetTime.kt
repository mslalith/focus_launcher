package dev.mslalith.focuslauncher.feature.lunarcalendar.detailsdialog

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaInstant

@Composable
internal fun RiseAndSetTime(
    modifier: Modifier = Modifier,
    localDateTime: LocalDateTime?,
    contentColor: Color
) {
    val time = if (localDateTime != null) {
        val javaInstant = localDateTime.toInstant(TimeZone.currentSystemDefault()).toJavaInstant()
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date.from(javaInstant))
    } else {
        "-"
    }
    Text(
        modifier = modifier,
        text = time,
        color = contentColor,
        style = MaterialTheme.typography.bodyMedium
    )
}
