package dev.mslalith.focuslauncher.feature.favorites.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun StaggeredFlowRow(
    modifier: Modifier = Modifier,
    mainAxisSpacing: Dp = 0.dp,
    crossAxisSpacing: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        val widths = measurables.map { it.maxIntrinsicWidth(height = constraints.maxHeight) }
        val totalWidth = constraints.maxWidth
        val mainAxisSpacingPx = mainAxisSpacing.roundToPx()
        val crossAxisSpacingPx = crossAxisSpacing.roundToPx()

        val remainingMeasurables = measurables.toMutableList()
        val remainingWidths = widths.toMutableList()
        val sequences = mutableListOf<List<Placeable>>()
        val crossAxisPositions = mutableListOf<Int>()

        while (remainingMeasurables.isNotEmpty()) {
            var currentWidth = 0
            var index = 0

            while (index < remainingWidths.size && (currentWidth + remainingWidths[index]) <= totalWidth) {
                currentWidth += remainingWidths[index++] + mainAxisSpacingPx
            }
            currentWidth -= mainAxisSpacingPx

            val remainingSpace = totalWidth - currentWidth
            val extraSpaceForEach = remainingSpace / index
            val placeables = remainingMeasurables
                .zip(other = remainingWidths)
                .take(n = index)
                .map { (measurable, width) ->
                    val newWidth = width + extraSpaceForEach
                    measurable.measure(
                        constraints = constraints.copy(
                            minWidth = newWidth,
                            maxWidth = newWidth
                        )
                    )
                }

            sequences.add(placeables)
            repeat(times = index) {
                remainingMeasurables.removeFirstOrNull()
                remainingWidths.removeFirstOrNull()
            }

            val crossAxisSpacingToAdd = if (remainingMeasurables.isNotEmpty()) crossAxisSpacingPx else 0
            crossAxisPositions.add(placeables.maxOf { it.height } + crossAxisSpacingToAdd)
        }

        val totalHeight = crossAxisPositions.sum()

        layout(
            width = totalWidth,
            height = totalHeight
        ) {
            var y = 0

            sequences.forEachIndexed { i, sequence ->
                val sequenceMainAxisSizes = sequence.map { it.width }.toIntArray()
                val mainAxisPositions = IntArray(sequenceMainAxisSizes.size) { 0 }

                with(Arrangement.spacedBy(space = mainAxisSpacing)) {
                    arrange(
                        totalSize = totalWidth,
                        sizes = sequenceMainAxisSizes,
                        outPositions = mainAxisPositions
                    )
                }

                mainAxisPositions.zip(other = sequence) { x, placeable ->
                    placeable.placeRelative(
                        x = x,
                        y = y
                    )
                }

                y += crossAxisPositions[i]
            }
        }
    }
}
