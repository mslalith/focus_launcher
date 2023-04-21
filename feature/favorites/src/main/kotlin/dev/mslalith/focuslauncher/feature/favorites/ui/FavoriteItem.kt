package dev.mslalith.focuslauncher.feature.favorites.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import dev.mslalith.focuslauncher.core.ui.model.AppWithIcon
import dev.mslalith.focuslauncher.feature.favorites.extensions.luminate
import dev.mslalith.focuslauncher.feature.favorites.model.FavoritesContextMode

@Composable
internal fun FavoriteItem(
    app: AppWithIcon,
    isInContextualMode: () -> Boolean,
    isAppAboutToReorder: () -> Boolean,
    changeFavoritesContextMode: (FavoritesContextMode) -> Unit,
    onClick: () -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.surface
    val contentColor = MaterialTheme.colorScheme.onSurface

    val color = remember(key1 = app) {
        val appIconPalette = Palette.from(app.icon.toBitmap()).generate()
        val extractedColor = Color(appIconPalette.getDominantColor(contentColor.toArgb()))
        return@remember extractedColor.luminate(threshold = 0.36f, value = 0.6f)
    }
    val animatedColor by animateColorAsState(
        label = "Animated color",
        targetValue = if (isAppAboutToReorder()) contentColor else color,
        animationSpec = tween(durationMillis = 600)
    )

    fun backgroundColor(): Color = animatedColor.copy(alpha = if (isAppAboutToReorder()) 0.8f else 0.23f)

    fun textColor(): Color = if (isAppAboutToReorder()) backgroundColor else animatedColor

    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .background(color = backgroundColor())
            .border(
                width = 0.25.dp,
                color = animatedColor,
                shape = MaterialTheme.shapes.small
            )
            .pointerInput(isInContextualMode()) {
                detectTapGestures(
                    onTap = { onClick() },
                    onLongPress = {
                        if (isInContextualMode()) return@detectTapGestures
                        changeFavoritesContextMode(FavoritesContextMode.Open)
                    }
                )
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = app.displayName,
            style = TextStyle(color = textColor())
        )
    }
}
