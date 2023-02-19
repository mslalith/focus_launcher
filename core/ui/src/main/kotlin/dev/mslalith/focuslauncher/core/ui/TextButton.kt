package dev.mslalith.focuslauncher.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun TextButton(
    modifier: Modifier = Modifier,
    text: String,
    backgroundColor: Color = MaterialTheme.colors.secondaryVariant,
    textColor: Color = MaterialTheme.colors.onBackground,
    paddingValues: PaddingValues = PaddingValues(horizontal = 18.dp, vertical = 8.dp),
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = backgroundColor)
            .clickable { onClick() }
            .padding(paddingValues = paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = TextStyle(
                color = textColor
            )
        )
    }
}
