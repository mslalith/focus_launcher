package dev.mslalith.focuslauncher.core.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectableIconItem(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes iconRes: Int,
    backgroundColor: Color = Color.Transparent,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    ListItem(
        modifier = modifier.clickable { onClick() },
        colors = ListItemDefaults.colors(
            containerColor = backgroundColor,
            headlineColor = contentColor
        ),
        leadingContent = {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = contentDescription ?: text,
                tint = contentColor
            )
        },
        headlineContent = { Text(text = text) }
    )
}
