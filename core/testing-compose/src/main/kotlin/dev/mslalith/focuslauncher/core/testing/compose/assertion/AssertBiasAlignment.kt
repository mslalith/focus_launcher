package dev.mslalith.focuslauncher.core.testing.compose.assertion

import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import dev.mslalith.focuslauncher.core.testing.compose.TestSemanticsProperties

fun SemanticsNodeInteraction.assertBiasAlignment(
    biasAlignment: BiasAlignment.Horizontal
): SemanticsNodeInteraction = assert(
    matcher = SemanticsMatcher.expectValue(
        key = TestSemanticsProperties.BiasAlignmentHorizontal,
        expectedValue = biasAlignment
    )
)

fun SemanticsNodeInteraction.assertBiasAlignment(
    biasAlignment: BiasAlignment.Vertical
): SemanticsNodeInteraction = assert(
    matcher = SemanticsMatcher.expectValue(
        key = TestSemanticsProperties.BiasAlignmentVertical,
        expectedValue = biasAlignment
    )
)
