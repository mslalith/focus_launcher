package dev.mslalith.focuslauncher.feature.lunarcalendar.settings

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.feature.lunarcalendar.widget.LunarCalendar

@Composable
internal fun PreviewLunarCalendar(
    showLunarPhase: Boolean,
    horizontalPadding: Dp = 24.dp
) {
    val height = 74.dp

    Crossfade(
        targetState = showLunarPhase,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding)
            .padding(top = 16.dp, bottom = 16.dp)
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = MaterialTheme.colors.secondaryVariant)
    ) {
        if (it) {
            LunarCalendar(height = height)
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = height),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Enable Lunar Phase to preview",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        color = MaterialTheme.colors.onBackground
                    )
                )
            }
        }
    }
}
