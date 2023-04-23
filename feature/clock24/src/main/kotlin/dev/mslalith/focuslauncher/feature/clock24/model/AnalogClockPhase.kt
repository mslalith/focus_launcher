package dev.mslalith.focuslauncher.feature.clock24.model

internal enum class AnalogClockPhase(
    val first: AnalogClockHandlePhase,
    val second: AnalogClockHandlePhase
) {
    NONE(first = AnalogClockHandlePhase.NONE, second = AnalogClockHandlePhase.NONE),
    TOP(first = AnalogClockHandlePhase.TOP, second = AnalogClockHandlePhase.TOP),
    RIGHT(first = AnalogClockHandlePhase.RIGHT, second = AnalogClockHandlePhase.RIGHT),
    BOTTOM(first = AnalogClockHandlePhase.BOTTOM, second = AnalogClockHandlePhase.BOTTOM),
    LEFT(first = AnalogClockHandlePhase.LEFT, second = AnalogClockHandlePhase.LEFT),
    VERTICAL(first = AnalogClockHandlePhase.TOP, second = AnalogClockHandlePhase.BOTTOM),
    TOP_LEFT(first = AnalogClockHandlePhase.TOP, second = AnalogClockHandlePhase.LEFT),
    TOP_RIGHT(first = AnalogClockHandlePhase.TOP, second = AnalogClockHandlePhase.RIGHT),
    BOTTOM_LEFT(first = AnalogClockHandlePhase.BOTTOM, second = AnalogClockHandlePhase.LEFT),
    BOTTOM_RIGHT(first = AnalogClockHandlePhase.BOTTOM, second = AnalogClockHandlePhase.RIGHT)
}
