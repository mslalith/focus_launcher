package dev.mslalith.focuslauncher.core.common.extensions

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale


fun Int.to2Digit() = when {
    this < 10 -> "0$this"
    else -> this
}

fun Double.asPercent(maxFractions: Int = 2) = limitDecimals(maxFractions = maxFractions) + "%"

fun Double.limitDecimals(
    maxFractions: Int = 2,
    minFractions: Int = 2
): String {
    val fractionsPlaceholder = "#".repeat(n = maxFractions)
    val decimalFormat = DecimalFormat("#.$fractionsPlaceholder", DecimalFormatSymbols(Locale.US)).apply {
        minimumFractionDigits = minFractions
        maximumFractionDigits = maxFractions
    }
    return decimalFormat.format(this) ?: this.toString()
}

fun Char.isAlphabet() = when (code) {
    in 65..90 -> true
    in 97..122 -> true
    else -> false
}
