package dev.mslalith.focuslauncher.core.testing.compose.matcher

import androidx.compose.ui.test.SemanticsMatcher
import dev.mslalith.focuslauncher.core.model.WidgetType
import dev.mslalith.focuslauncher.core.testing.compose.TestSemanticsProperties

fun hasWidgetType(
    widgetType: WidgetType
): SemanticsMatcher = SemanticsMatcher.expectValue(
    key = TestSemanticsProperties.WidgetType,
    expectedValue = widgetType
)
