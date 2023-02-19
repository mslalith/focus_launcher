package dev.mslalith.focuslauncher.feature.lunarcalendar.detailsdialog

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
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
    textColor: Color = MaterialTheme.colors.onBackground
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
        style = TextStyle(
            color = textColor,
            fontSize = 14.sp,
            letterSpacing = 1.2.sp
        )
    )
}
