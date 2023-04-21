package dev.mslalith.focuslauncher.core.ui.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.unit.Dp

fun Modifier.verticalFadeOutEdge(
    height: Dp,
    color: Color
): Modifier = this.then(
    other = VerticalFadeOutEdge(
        height = height,
        color = color
    )
)

private class VerticalFadeOutEdge constructor(
    private val height: Dp,
    private val color: Color
) : DrawModifier {

    override fun ContentDrawScope.draw() {
        drawContent()

        val colors = listOf(color, Color.Transparent)
        val heightPx = height.toPx()

        drawRect(
            brush = Brush.verticalGradient(
                colors = colors,
                startY = 0f,
                endY = heightPx
            )
        )

        drawRect(
            brush = Brush.verticalGradient(
                colors = colors.reversed(),
                startY = size.height - heightPx,
                endY = size.height
            )
        )
    }
}
