package dev.mslalith.focuslauncher.feature.settingspage.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun <T> SettingsGridContent(
    modifier: Modifier = Modifier,
    itemModifier: Modifier = Modifier,
    columnSize: Int = 2,
    items: ImmutableList<T>,
    content: @Composable (item: T) -> Unit
) {
    val chunkedItems by remember(key1 = items) {
        derivedStateOf { items.chunked(size = columnSize) }
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        chunkedItems.forEach { rowItems ->
            Row {
                rowItems.forEach { item ->
                    Box(
                        modifier = itemModifier.weight(weight = 1f)
                    ) {
                        content(item)
                    }
                }

                if (rowItems.size < columnSize) {
                    val count = columnSize - rowItems.size
                    repeat(times = count) {
                        Box(modifier = itemModifier.weight(weight = 1f))
                    }
                }
            }
        }
    }
}
