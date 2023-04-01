package dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics

import androidx.compose.ui.BiasAlignment
import dev.mslalith.focuslauncher.core.model.SelectedApp

interface TestSemanticsScope {
    fun testBiasAlignment(biasAlignment: BiasAlignment.Horizontal)
    fun testBiasAlignment(biasAlignment: BiasAlignment.Vertical)

    fun testSelectedApp(selectedApp: SelectedApp)
}
