package dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics

import androidx.compose.ui.BiasAlignment
import dev.mslalith.focuslauncher.core.model.WidgetType
import dev.mslalith.focuslauncher.core.model.app.SelectedApp
import dev.mslalith.focuslauncher.core.model.app.SelectedHiddenApp

interface TestSemanticsScope {
    fun testString(value: String)
    fun testBoolean(value: Boolean)

    fun testBiasAlignment(biasAlignment: BiasAlignment.Horizontal)
    fun testBiasAlignment(biasAlignment: BiasAlignment.Vertical)

    fun testSelectedApp(selectedApp: SelectedApp)
    fun testSelectedHiddenApp(selectedHiddenApp: SelectedHiddenApp)

    fun testWidgetType(widgetType: WidgetType)
}
