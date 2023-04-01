package dev.mslalith.focuslauncher.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExtendedMiniFab(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colors

    Row(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(percent = 50))
            .clickable { onClick() }
            .background(colors.onBackground.copy(alpha = 0.85f))
            .padding(horizontal = 12.dp)
    ) {
        val centerVerticallyModifier = Modifier.align(Alignment.CenterVertically)

        Icon(
            imageVector = icon,
            contentDescription = contentDescription ?: text,
            tint = colors.background,
            modifier = centerVerticallyModifier.padding(vertical = 10.dp)
        )
        Text(
            text = text,
            style = TextStyle(
                color = colors.background,
                fontSize = 13.sp
            ),
            modifier = centerVerticallyModifier.padding(horizontal = 8.dp)
        )
    }
}
