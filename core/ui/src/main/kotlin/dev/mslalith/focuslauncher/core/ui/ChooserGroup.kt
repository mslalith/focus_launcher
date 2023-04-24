package dev.mslalith.focuslauncher.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ChooserGroup(
    modifier: Modifier = Modifier,
    textIconsList: List<Pair<String, Int>>,
    selectedItem: String,
    showText: Boolean = true,
    itemHorizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    onItemSelected: (Int) -> Unit
) {
    Row(
        modifier = modifier
    ) {
        textIconsList.forEachIndexed { index, textIcon ->
            val isSelected = selectedItem == textIcon.first
            val backgroundColor by animateColorAsState(
                label = "Background Color",
                targetValue = if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent,
                animationSpec = tween(durationMillis = 300)
            )

            val contentColor by animateColorAsState(
                label = "Content Color",
                targetValue = if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurface,
                animationSpec = tween(durationMillis = 300)
            )

            TextIconButton(
                text = if (showText) textIcon.first else null,
                icon = textIcon.second,
                backgroundColor = backgroundColor,
                contentColor = contentColor,
                horizontalArrangement = itemHorizontalArrangement,
                onClick = {
                    if (!isSelected) onItemSelected(index)
                },
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .weight(weight = 1f)
            )
        }
    }
}
