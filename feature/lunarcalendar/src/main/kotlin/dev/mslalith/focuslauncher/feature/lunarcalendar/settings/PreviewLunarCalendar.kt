package dev.mslalith.focuslauncher.feature.lunarcalendar.settings

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.feature.lunarcalendar.widget.LunarCalendar

@Composable
internal fun PreviewLunarCalendar(
    showLunarPhase: Boolean,
    horizontalPadding: Dp = 24.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer
) {
    val height = 74.dp

    Crossfade(
        label = "Cross Fade Preview Lunar Calendar",
        targetState = showLunarPhase,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding)
            .padding(top = 16.dp, bottom = 16.dp)
            .clip(shape = MaterialTheme.shapes.small)
    ) {
        if (it) {
            LunarCalendar(
                height = height,
                backgroundColor = backgroundColor,
                contentColor = contentColor
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = height)
                    .background(color = backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Enable Lunar Phase to preview",
                    textAlign = TextAlign.Center,
                    color = contentColor,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
