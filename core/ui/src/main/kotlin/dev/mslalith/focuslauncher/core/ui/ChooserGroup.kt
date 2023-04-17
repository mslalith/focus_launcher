package dev.mslalith.focuslauncher.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
    val selectedColor = MaterialTheme.colors.secondaryVariant

    Row(
        modifier = modifier
    ) {
        textIconsList.forEachIndexed { index, textIcon ->
            val isSelected = selectedItem == textIcon.first
            val backgroundColorAlpha by animateFloatAsState(
                label = "Background Color Alpha",
                targetValue = if (isSelected) 1f else 0f,
                animationSpec = tween(durationMillis = 300)
            )

            TextIconButton(
                text = if (showText) textIcon.first else null,
                icon = painterResource(id = textIcon.second),
                backgroundColor = selectedColor.copy(alpha = backgroundColorAlpha),
                horizontalArrangement = itemHorizontalArrangement,
                onClick = {
                    if (!isSelected) {
                        onItemSelected(index)
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .weight(weight = 1f)
            )
        }
    }
}
