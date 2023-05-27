package dev.mslalith.focuslauncher.feature.favorites.extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

internal fun Color.luminate(threshold: Float, value: Float) = toHSL().apply {
    val luminance = this[2]
    this[2] = if (luminance < threshold) value else luminance
}.toColor()

fun Color.blendWith(color: Color, ratio: Float): Color = ColorUtils.blendARGB(toArgb(), color.toArgb(), ratio).let(::Color)

internal fun Color.toHSL(): FloatArray {
    val hsl = FloatArray(size = 3)
    val min = minOf(red, green, blue)
    val max = maxOf(red, green, blue)
    hsl[2] = (max + min) / 2

    if (max == min) {
        hsl[1] = 0f
        hsl[0] = hsl[1]
    } else {
        val d = max - min
        hsl[1] = if (hsl[2] > 0.5f) d / (2f - max - min) else d / (max + min)
        when (max) {
            red -> hsl[0] = (green - blue) / d + (if (green < blue) 6 else 0)
            green -> hsl[0] = (blue - red) / d + 2
            blue -> hsl[0] = (red - green) / d + 4
        }
        hsl[0] /= 6f
    }
    return hsl
}

internal fun FloatArray.toColor(): Color {
    val r: Float
    val g: Float
    val b: Float

    val h = this[0]
    val s = this[1]
    val l = this[2]

    if (s == 0f) {
        b = l
        g = b
        r = g
    } else {
        val q = if (l < 0.5f) l * (1 + s) else l + s - l * s
        val p = 2 * l - q
        r = hue2rgb(p, q, h + 1f / 3)
        g = hue2rgb(p, q, h)
        b = hue2rgb(p, q, h - 1f / 3)
    }

    return Color((r * 255).toInt(), (g * 255).toInt(), (b * 255).toInt())
}

private fun hue2rgb(p: Float, q: Float, t: Float): Float {
    var valueT = t
    if (valueT < 0) valueT += 1f
    if (valueT > 1) valueT -= 1f
    if (valueT < 1f / 6) return p + (q - p) * 6f * valueT
    if (valueT < 1f / 2) return q
    return if (valueT < 2f / 3) p + (q - p) * (2f / 3 - valueT) * 6f else p
}
