package dev.mslalith.focuslauncher.core.testing.compose.matcher

import androidx.compose.ui.test.SemanticsMatcher
import dev.mslalith.focuslauncher.core.model.SelectedApp
import dev.mslalith.focuslauncher.core.testing.compose.TestSemanticsProperties

fun hasSelectedApp(
    selectedApp: SelectedApp
): SemanticsMatcher = SemanticsMatcher.expectValue(
    key = TestSemanticsProperties.SelectedApp,
    expectedValue = selectedApp
)
