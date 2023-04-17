package dev.mslalith.focuslauncher.core.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.ui.ChooserGroup

@Composable
fun SettingsSelectableChooserItem(
    modifier: Modifier = Modifier,
    text: String,
    subText: String,
    textIconsList: List<Pair<String, Int>>,
    selectedItem: String,
    showText: Boolean = true,
    itemHorizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    onItemSelected: (Int) -> Unit,
    disabled: Boolean = false,
    height: Dp = 56.dp,
    horizontalPadding: Dp = 24.dp
) {
    SettingsSelectableBottomContentItem(
        modifier = modifier,
        text = text,
        subText = subText,
        disabled = disabled,
        height = height,
        horizontalPadding = horizontalPadding
    ) {
        ChooserGroup(
            textIconsList = textIconsList,
            onItemSelected = onItemSelected,
            selectedItem = selectedItem,
            showText = showText,
            itemHorizontalArrangement = itemHorizontalArrangement,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}
