package dev.mslalith.focuslauncher.feature.clock24.model

import dev.mslalith.focuslauncher.feature.clock24.model.AnalogClockPhase.VERTICAL
import dev.mslalith.focuslauncher.feature.clock24.model.AnalogClockPhase.BOTTOM
import dev.mslalith.focuslauncher.feature.clock24.model.AnalogClockPhase.BOTTOM_LEFT
import dev.mslalith.focuslauncher.feature.clock24.model.AnalogClockPhase.BOTTOM_RIGHT
import dev.mslalith.focuslauncher.feature.clock24.model.AnalogClockPhase.LEFT
import dev.mslalith.focuslauncher.feature.clock24.model.AnalogClockPhase.NONE
import dev.mslalith.focuslauncher.feature.clock24.model.AnalogClockPhase.RIGHT
import dev.mslalith.focuslauncher.feature.clock24.model.AnalogClockPhase.TOP
import dev.mslalith.focuslauncher.feature.clock24.model.AnalogClockPhase.TOP_LEFT
import dev.mslalith.focuslauncher.feature.clock24.model.AnalogClockPhase.TOP_RIGHT

internal data class Digit(
    val analogHandles: List<List<AnalogClockPhase>>
) {
    companion object {
        private val ZERO = Digit(
            analogHandles = listOf(
                listOf(BOTTOM_RIGHT, BOTTOM_LEFT),
                listOf(VERTICAL, VERTICAL),
                listOf(TOP_RIGHT, TOP_LEFT)
            )
        )
        private val ONE = Digit(
            analogHandles = listOf(
                listOf(NONE, BOTTOM),
                listOf(NONE, VERTICAL),
                listOf(NONE, TOP)
            )
        )
        private val TWO = Digit(
            analogHandles = listOf(
                listOf(RIGHT, BOTTOM_LEFT),
                listOf(BOTTOM_RIGHT, TOP_LEFT),
                listOf(TOP_RIGHT, LEFT)
            )
        )
        private val THREE = Digit(
            analogHandles = listOf(
                listOf(RIGHT, BOTTOM_LEFT),
                listOf(RIGHT, TOP_LEFT),
                listOf(RIGHT, TOP_LEFT)
            )
        )
        private val FOUR = Digit(
            analogHandles = listOf(
                listOf(BOTTOM, BOTTOM),
                listOf(TOP_RIGHT, TOP_LEFT),
                listOf(NONE, TOP)
            )
        )
        private val FIVE = Digit(
            analogHandles = listOf(
                listOf(BOTTOM_RIGHT, LEFT),
                listOf(TOP_RIGHT, BOTTOM_LEFT),
                listOf(RIGHT, TOP_LEFT)
            )
        )
        private val SIX = Digit(
            analogHandles = listOf(
                listOf(BOTTOM_RIGHT, LEFT),
                listOf(VERTICAL, BOTTOM_LEFT),
                listOf(TOP_RIGHT, TOP_LEFT)
            )
        )
        private val SEVEN = Digit(
            analogHandles = listOf(
                listOf(RIGHT, BOTTOM_LEFT),
                listOf(NONE, VERTICAL),
                listOf(NONE, TOP)
            )
        )
        private val EIGHT = Digit(
            analogHandles = listOf(
                listOf(BOTTOM_RIGHT, BOTTOM_LEFT),
                listOf(TOP_RIGHT, TOP_LEFT),
                listOf(TOP_RIGHT, TOP_LEFT)
            )
        )
        private val NINE = Digit(
            analogHandles = listOf(
                listOf(BOTTOM_RIGHT, BOTTOM_LEFT),
                listOf(TOP_RIGHT, VERTICAL),
                listOf(RIGHT, TOP_LEFT)
            )
        )
        val ALL = listOf(ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE)
    }
}
