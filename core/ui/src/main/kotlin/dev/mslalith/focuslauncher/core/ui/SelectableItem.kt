package dev.mslalith.focuslauncher.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SelectableItem(
    modifier: Modifier = Modifier,
    text: String,
    leading: @Composable () -> Unit,
    height: Dp = 48.dp,
    iconWidth: Dp = 56.dp,
    onClick: (() -> Unit)?
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(height = height)
            .clickable(enabled = onClick != null) { onClick?.invoke() }
    ) {
        Box(
            modifier = Modifier.size(
                width = iconWidth,
                height = height
            ),
            contentAlignment = Alignment.Center
        ) {
            leading()
        }
        Text(
            text = text,
            style = TextStyle(
                color = MaterialTheme.colors.onBackground,
                fontSize = 16.sp
            ),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 8.dp, end = 8.dp)
        )
    }
}
