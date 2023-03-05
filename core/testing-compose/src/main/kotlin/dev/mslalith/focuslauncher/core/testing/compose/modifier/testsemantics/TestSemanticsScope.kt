package dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics

import androidx.compose.ui.BiasAlignment

interface TestSemanticsScope {
    fun testBiasAlignment(biasAlignment: BiasAlignment.Horizontal)
    fun testBiasAlignment(biasAlignment: BiasAlignment.Vertical)
}
