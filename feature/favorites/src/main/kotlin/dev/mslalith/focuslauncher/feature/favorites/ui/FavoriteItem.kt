package dev.mslalith.focuslauncher.feature.favorites.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.model.app.AppWithColor
import dev.mslalith.focuslauncher.core.ui.remember.rememberAppColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun FavoriteItem(
    appWithColor: AppWithColor,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appIconBasedColor = rememberAppColor(graphicsColor = appWithColor.color)

    Row(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = appIconBasedColor.copy(alpha = 0.23f))
            .border(
                width = 0.4.dp,
                color = appIconBasedColor.copy(alpha = 0.6f),
                shape = MaterialTheme.shapes.small
            )
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = appWithColor.app.displayName,
            color = appIconBasedColor,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
