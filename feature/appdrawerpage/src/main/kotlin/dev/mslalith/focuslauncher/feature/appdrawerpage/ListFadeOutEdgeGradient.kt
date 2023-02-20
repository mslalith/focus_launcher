package dev.mslalith.focuslauncher.feature.appdrawerpage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.feature.appdrawerpage.model.Position

@Composable
internal fun ListFadeOutEdgeGradient(
    position: Position,
    height: Dp = 14.dp
) {
    val colors = listOf(MaterialTheme.colors.background, Color.Transparent).let {
        when (position) {
            Position.TOP -> it
            Position.BOTTOM -> it.reversed()
        }
    }
    val alignment = when (position) {
        Position.TOP -> Alignment.TopCenter
        Position.BOTTOM -> Alignment.BottomCenter
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Spacer(
            modifier = Modifier
                .align(alignment)
                .fillMaxWidth()
                .height(height)
                .background(brush = Brush.verticalGradient(colors))
        )
    }
}
