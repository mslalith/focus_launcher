package dev.mslalith.focuslauncher.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextIconButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: Painter,
    contentDescription: String? = null,
    paddingValues: PaddingValues = PaddingValues(
        horizontal = 12.dp,
        vertical = 8.dp
    ),
    afterIconSpacing: Dp = 14.dp,
    backgroundColor: Color = Color.Transparent,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = backgroundColor)
            .clickable { onClick() }
            .padding(paddingValues = paddingValues)
    ) {
        Icon(
            painter = icon,
            contentDescription = contentDescription ?: text,
            tint = MaterialTheme.colors.onBackground
        )
        HorizontalSpacer(spacing = afterIconSpacing)
        Text(
            text = text,
            style = TextStyle(
                color = MaterialTheme.colors.onBackground,
                fontSize = 16.sp
            )
        )
    }
}
