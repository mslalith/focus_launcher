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
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.testSemantics
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ChooserGroup(
    modifier: Modifier = Modifier,
    textIconsList: ImmutableList<Pair<String, Int>>,
    selectedItem: String,
    showText: Boolean = true,
    itemHorizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    onItemSelected: (Int) -> Unit
) {
    Row(
        modifier = modifier
    ) {
        textIconsList.forEachIndexed { index, (text, icon) ->
            val isSelected = selectedItem == text
            val backgroundColor by animateColorAsState(
                label = "Background Color",
                targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                animationSpec = tween(durationMillis = 300)
            )

            val contentColor by animateColorAsState(
                label = "Content Color",
                targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                animationSpec = tween(durationMillis = 300)
            )

            TextIconButton(
                text = if (showText) text else null,
                icon = icon,
                backgroundColor = backgroundColor,
                contentColor = contentColor,
                horizontalArrangement = itemHorizontalArrangement,
                onClick = {
                    if (!isSelected) onItemSelected(index)
                },
                modifier = Modifier
                    .testSemantics(tag = text)
                    .padding(horizontal = 4.dp)
                    .weight(weight = 1f)
            )
        }
    }
}
