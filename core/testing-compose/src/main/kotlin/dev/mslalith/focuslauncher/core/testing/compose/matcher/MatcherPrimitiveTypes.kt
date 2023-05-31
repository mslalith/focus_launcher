package dev.mslalith.focuslauncher.core.testing.compose.matcher

import androidx.compose.ui.test.SemanticsMatcher
import dev.mslalith.focuslauncher.core.testing.compose.TestSemanticsProperties

fun hasString(
    value: String
): SemanticsMatcher = SemanticsMatcher.expectValue(
    key = TestSemanticsProperties.StringType,
    expectedValue = value
)

fun hasBoolean(
    value: Boolean
): SemanticsMatcher = SemanticsMatcher.expectValue(
    key = TestSemanticsProperties.BooleanType,
    expectedValue = value
)
