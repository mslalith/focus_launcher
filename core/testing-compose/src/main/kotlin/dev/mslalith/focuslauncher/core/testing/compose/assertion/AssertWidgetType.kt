package dev.mslalith.focuslauncher.core.testing.compose.assertion

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import dev.mslalith.focuslauncher.core.model.WidgetType
import dev.mslalith.focuslauncher.core.testing.compose.TestSemanticsProperties

fun SemanticsNodeInteraction.assertWidgetType(
    widgetType: WidgetType
): SemanticsNodeInteraction = assert(
    matcher = SemanticsMatcher.expectValue(
        key = TestSemanticsProperties.WidgetType,
        expectedValue = widgetType
    )
)
