package dev.mslalith.focuslauncher.core.testing.compose.assertion

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import dev.mslalith.focuslauncher.core.testing.compose.TestSemanticsProperties

fun SemanticsNodeInteraction.assertStringType(
    value: String
): SemanticsNodeInteraction = assert(
    matcher = SemanticsMatcher.expectValue(
        key = TestSemanticsProperties.StringType,
        expectedValue = value
    )
)

fun SemanticsNodeInteraction.assertBooleanType(
    value: Boolean
): SemanticsNodeInteraction = assert(
    matcher = SemanticsMatcher.expectValue(
        key = TestSemanticsProperties.BooleanType,
        expectedValue = value
    )
)
