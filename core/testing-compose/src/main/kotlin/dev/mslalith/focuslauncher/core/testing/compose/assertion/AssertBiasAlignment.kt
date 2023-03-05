package dev.mslalith.focuslauncher.core.testing.compose.assertion

import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import dev.mslalith.focuslauncher.core.testing.compose.TestSemanticsProperties

fun SemanticsNodeInteraction.assertBiasAlignment(
    biasAlignment: BiasAlignment.Horizontal
): SemanticsNodeInteraction =
    assert(SemanticsMatcher.expectValue(TestSemanticsProperties.BiasAlignmentHorizontal, biasAlignment))

fun SemanticsNodeInteraction.assertBiasAlignment(
    biasAlignment: BiasAlignment.Vertical
): SemanticsNodeInteraction =
    assert(SemanticsMatcher.expectValue(TestSemanticsProperties.BiasAlignmentVertical, biasAlignment))
