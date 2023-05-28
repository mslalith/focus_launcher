package dev.mslalith.focuslauncher.core.ui.remember

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import dev.mslalith.focuslauncher.core.ui.extensions.blendWith
import dev.mslalith.focuslauncher.core.ui.extensions.luminate
import android.graphics.Color as GraphicsColor

@Composable
fun rememberAppColor(graphicsColor: GraphicsColor?): Color {
    val backgroundColor = MaterialTheme.colorScheme.surface
    val contentColor = MaterialTheme.colorScheme.onSurface
    val primaryColor = MaterialTheme.colorScheme.primary

    return remember(graphicsColor, primaryColor, backgroundColor, contentColor) {
        var color = graphicsColor?.toArgb()?.let(::Color) ?: primaryColor
        color = color.luminate(threshold = 0.36f, value = 0.6f)

        val contrastThreshold = 2.5f
        val contrast = ColorUtils.calculateContrast(color.toArgb(), backgroundColor.toArgb()).toFloat()

        if (contrast < contrastThreshold) color.blendWith(
            color = contentColor,
            ratio = (contrastThreshold - contrast) / contrastThreshold
        ) else color
    }
}
