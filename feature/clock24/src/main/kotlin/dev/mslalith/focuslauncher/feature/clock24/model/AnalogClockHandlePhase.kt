package dev.mslalith.focuslauncher.feature.clock24.model

internal enum class AnalogClockHandlePhase(val angle: Double) {
    NONE(angle = 135.0),
    TOP(angle = 270.0),
    RIGHT(angle = 0.0),
    BOTTOM(angle = 90.0),
    LEFT(angle = 180.0)
}
