package dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.impl

import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import dev.mslalith.focuslauncher.core.model.SelectedApp
import dev.mslalith.focuslauncher.core.testing.compose.TestSemanticsProperties
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.TestSemanticsScope

internal class TestSemanticsScopeImpl(
    private val semanticsPropertyReceiver: SemanticsPropertyReceiver
) : TestSemanticsScope {

    override fun testBiasAlignment(
        biasAlignment: BiasAlignment.Horizontal
    ) = with(semanticsPropertyReceiver) {
        this[TestSemanticsProperties.BiasAlignmentHorizontal] = biasAlignment
    }

    override fun testBiasAlignment(
        biasAlignment: BiasAlignment.Vertical
    ) = with(semanticsPropertyReceiver) {
        this[TestSemanticsProperties.BiasAlignmentVertical] = biasAlignment
    }

    override fun testSelectedApp(selectedApp: SelectedApp) = with(semanticsPropertyReceiver) {
        this[TestSemanticsProperties.SelectedApp] = selectedApp
    }
}
