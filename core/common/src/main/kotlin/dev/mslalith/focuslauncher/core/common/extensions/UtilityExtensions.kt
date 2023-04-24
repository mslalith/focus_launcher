package dev.mslalith.focuslauncher.core.common.extensions

fun Int.to2Digit() = when {
    this < 10 -> "0$this"
    else -> this
}

fun Double.asPercent(precision: Int = 2) = "%.${precision}f".format(this) + "%"
fun Double.limitDecimals(precision: Int = 2) = "%.${precision}f".format(this)

fun Char.isAlphabet() = when (code) {
    in 65..90 -> true
    in 97..122 -> true
    else -> false
}
