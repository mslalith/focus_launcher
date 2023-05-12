package dev.mslalith.focuslauncher.core.ui.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.unit.Dp

fun Modifier.horizontalFadeOutEdge(
    width: Dp,
    color: Color
): Modifier = this.then(
    other = HorizontalFadeOutEdge(
        width = width,
        color = color
    )
)

private class HorizontalFadeOutEdge(
    private val width: Dp,
    private val color: Color
) : DrawModifier {

    override fun ContentDrawScope.draw() {
        drawContent()

        val colors = listOf(color, Color.Transparent)
        val widthPx = width.toPx()

        drawRect(
            brush = Brush.horizontalGradient(
                colors = colors,
                startX = 0f,
                endX = widthPx
            )
        )

        drawRect(
            brush = Brush.horizontalGradient(
                colors = colors.reversed(),
                startX = size.width - widthPx,
                endX = size.width
            )
        )
    }
}
