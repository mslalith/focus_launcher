package dev.mslalith.focuslauncher.ui.views.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import dev.mslalith.focuslauncher.core.model.lunarphase.LunarPhaseDetails
import dev.mslalith.focuslauncher.core.model.lunarphase.NextPhaseDetails
import dev.mslalith.focuslauncher.data.model.getOrNull
import dev.mslalith.focuslauncher.extensions.FillSpacer
import dev.mslalith.focuslauncher.extensions.HorizontalSpacer
import dev.mslalith.focuslauncher.extensions.VerticalSpacer
import dev.mslalith.focuslauncher.extensions.asPercent
import dev.mslalith.focuslauncher.extensions.inShortReadableFormat
import dev.mslalith.focuslauncher.extensions.limitDecimals
import dev.mslalith.focuslauncher.ui.viewmodels.WidgetsViewModel
import dev.mslalith.focuslauncher.ui.views.widgets.LunarPhaseMoonIcon
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaInstant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LunarPhaseDetailsDialog(
    widgetsViewModel: WidgetsViewModel,
    onClose: () -> Unit
) {
    Dialog(
        onDismissRequest = onClose
    ) {
        val lunarPhaseDetails by widgetsViewModel.lunarPhaseDetailsStateFlow.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(size = 12.dp))
                .background(color = MaterialTheme.colors.primaryVariant)
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            lunarPhaseDetails.getOrNull()?.let { phaseDetails ->
                TodayLunarPhase(lunarPhaseDetails = phaseDetails)

                Divider(
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.1f),
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                NextMajorPhaseDetails(phaseDetails.nextPhaseDetails)

                Divider(
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.1f),
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                LunarRiseAndSetDetails(lunarPhaseDetails = phaseDetails)
            }
        }
    }
}

@Composable
private fun TodayLunarPhase(
    lunarPhaseDetails: LunarPhaseDetails
) {
    BoxWithConstraints {
        val width = maxWidth
        Row(modifier = Modifier.fillMaxWidth()) {
            TodayLunarMoonIconAndPhase(
                lunarPhaseDetails = lunarPhaseDetails,
                moonSize = width
            )
            HorizontalSpacer(spacing = 12.dp)
            TodayLunarMoonPhaseDetails(lunarPhaseDetails = lunarPhaseDetails)
        }
    }
}

@Composable
private fun TodayLunarMoonIconAndPhase(
    lunarPhaseDetails: LunarPhaseDetails,
    moonSize: Dp
) {
    Column {
        LunarPhaseMoonIcon(
            phaseAngle = lunarPhaseDetails.phaseAngle,
            illumination = lunarPhaseDetails.illumination,
            moonSize = moonSize * 0.3f
        )
    }
}

@Composable
private fun TodayLunarMoonPhaseDetails(
    lunarPhaseDetails: LunarPhaseDetails,
    textColor: Color = MaterialTheme.colors.onBackground
) {
    Column {
        Text(
            text = lunarPhaseDetails.lunarPhase.phaseName,
            style = TextStyle(
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.2.sp
            )
        )
        VerticalSpacer(spacing = 8.dp)
        Text(
            text = "Illumination: ${lunarPhaseDetails.illumination.times(100).asPercent(precision = 3)}",
            style = TextStyle(
                color = textColor,
                fontSize = 14.sp,
                letterSpacing = 1.2.sp
            )
        )
        VerticalSpacer(spacing = 4.dp)
        Text(
            text = "Angle: ${lunarPhaseDetails.phaseAngle.limitDecimals(precision = 2)}",
            style = TextStyle(
                color = textColor,
                fontSize = 14.sp,
                letterSpacing = 1.2.sp
            )
        )
    }
}

@Composable
private fun NextMajorPhaseDetails(
    nextPhaseDetails: NextPhaseDetails,
    textColor: Color = MaterialTheme.colors.onBackground
) {
    Column {
        Text(
            text = "Upcoming Phases",
            style = TextStyle(
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.2.sp
            )
        )

        VerticalSpacer(spacing = 12.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(intrinsicSize = IntrinsicSize.Min)
        ) {
            Box(modifier = Modifier.weight(weight = 1f)) {
                NextSingleMajorPhaseDetails(
                    illumination = 0.0,
                    localDateTime = nextPhaseDetails.newMoon
                )
            }

            HorizontalSpacer(spacing = 12.dp)

            Box(modifier = Modifier.weight(weight = 1f)) {
                NextSingleMajorPhaseDetails(
                    illumination = 100.0,
                    localDateTime = nextPhaseDetails.fullMoon
                )
            }
        }
    }
}

@Composable
private fun NextSingleMajorPhaseDetails(
    illumination: Double,
    localDateTime: LocalDateTime?,
    textColor: Color = MaterialTheme.colors.onBackground
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
                style = TextStyle(
                    color = textColor,
                    fontSize = 14.sp,
                    letterSpacing = 1.2.sp
                )
            )

            VerticalSpacer(spacing = 4.dp)

            Text(
                text = time,
                style = TextStyle(
                    color = textColor,
                    fontSize = 11.sp,
                    letterSpacing = 1.2.sp
                )
            )
        }
    }
}

@Composable
fun LunarRiseAndSetDetails(
    lunarPhaseDetails: LunarPhaseDetails
) {
    RiseAndSetHeaders()
    VerticalSpacer(spacing = 16.dp)
    RiseTimeDetails(
        moonRiseDateTime = lunarPhaseDetails.moonRiseAndSetDetails.riseDateTime,
        sunRiseDateTime = lunarPhaseDetails.sunRiseAndSetDetails.riseDateTime
    )
    VerticalSpacer(spacing = 4.dp)
    SetTimeDetails(
        moonSetDateTime = lunarPhaseDetails.moonRiseAndSetDetails.setDateTime,
        sunSetDateTime = lunarPhaseDetails.sunRiseAndSetDetails.setDateTime
    )
}

@Composable
private fun RiseAndSetHeaders(
    textColor: Color = MaterialTheme.colors.onBackground
) {
    Row {
        Text(
            text = "Moon",
            style = TextStyle(
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.2.sp
            )
        )
        FillSpacer()
        Text(
            text = "Sun",
            style = TextStyle(
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.2.sp
            )
        )
    }
}

@Composable
private fun RiseTimeDetails(
    moonRiseDateTime: LocalDateTime?,
    sunRiseDateTime: LocalDateTime?
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RiseAndSetTime(localDateTime = moonRiseDateTime)
        RiseAndSetIndicator(text = "Rise")
        RiseAndSetTime(localDateTime = sunRiseDateTime)
    }
}

@Composable
private fun SetTimeDetails(
    moonSetDateTime: LocalDateTime?,
    sunSetDateTime: LocalDateTime?
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RiseAndSetTime(localDateTime = moonSetDateTime)
        RiseAndSetIndicator(text = "Set")
        RiseAndSetTime(localDateTime = sunSetDateTime)
    }
}

@Composable
private fun RiseAndSetTime(
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

@Composable
private fun RowScope.RiseAndSetIndicator(
    text: String,
    textColor: Color = MaterialTheme.colors.onBackground
) {
    Box(
        modifier = Modifier.weight(weight = 1f),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = TextStyle(
                color = textColor,
                fontSize = 12.sp,
                letterSpacing = 1.2.sp
            )
        )
    }
}
