package dev.mslalith.focuslauncher.core.testing.extensions

import java.util.Locale

fun withLocale(locale: Locale, block: () -> Unit) {
    val originalLocale = Locale.getDefault()
    Locale.setDefault(locale)
    block()
    Locale.setDefault(originalLocale)
}
