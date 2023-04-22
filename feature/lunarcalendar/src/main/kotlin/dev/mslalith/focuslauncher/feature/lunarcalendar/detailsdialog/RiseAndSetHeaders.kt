package dev.mslalith.focuslauncher.feature.lunarcalendar.detailsdialog

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dev.mslalith.focuslauncher.core.ui.FillSpacer

@Composable
internal fun RiseAndSetHeaders(
    contentColor: Color
) {
    Row {
        Text(
            text = "Moon",
            color = contentColor,
            style = MaterialTheme.typography.titleMedium
        )
        FillSpacer()
        Text(
            text = "Sun",
            color = contentColor,
            style = MaterialTheme.typography.titleMedium
        )
    }
}
