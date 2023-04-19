package dev.mslalith.focuslauncher.feature.lunarcalendar.detailsdialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
internal fun RowScope.RiseAndSetIndicator(
    text: String,
    contentColor: Color
) {
    Box(
        modifier = Modifier.weight(weight = 1f),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = TextStyle(
                color = contentColor,
                fontSize = 12.sp,
                letterSpacing = 1.2.sp
            )
        )
    }
}
