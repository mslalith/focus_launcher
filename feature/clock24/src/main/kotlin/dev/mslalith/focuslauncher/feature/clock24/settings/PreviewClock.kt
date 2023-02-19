package dev.mslalith.focuslauncher.feature.clock24.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.feature.clock24.ClockWidget

@Composable
internal fun PreviewClock() {
    val height = 134.dp
    val horizontalPadding = 24.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = height)
            .padding(horizontal = horizontalPadding)
            .padding(top = 16.dp, bottom = 16.dp)
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = MaterialTheme.colors.secondaryVariant)
    ) {
        ClockWidget(
            horizontalPadding = 22.dp,
            centerVertically = true
        )
    }
}
