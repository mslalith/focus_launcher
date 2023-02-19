package dev.mslalith.focuslauncher.feature.lunarcalendar.detailsdialog

import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.mslalith.focuslauncher.core.ui.FillSpacer

@Composable
internal fun RiseAndSetHeaders(
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
