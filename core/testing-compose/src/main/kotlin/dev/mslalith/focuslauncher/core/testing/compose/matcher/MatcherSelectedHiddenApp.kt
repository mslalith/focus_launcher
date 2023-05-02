package dev.mslalith.focuslauncher.core.testing.compose.matcher

import androidx.compose.ui.test.SemanticsMatcher
import dev.mslalith.focuslauncher.core.model.app.SelectedHiddenApp
import dev.mslalith.focuslauncher.core.testing.compose.TestSemanticsProperties

fun hasSelectedHiddenApp(
    selectedHiddenApp: SelectedHiddenApp
): SemanticsMatcher = SemanticsMatcher.expectValue(
    key = TestSemanticsProperties.SelectedHiddenApp,
    expectedValue = selectedHiddenApp
)
