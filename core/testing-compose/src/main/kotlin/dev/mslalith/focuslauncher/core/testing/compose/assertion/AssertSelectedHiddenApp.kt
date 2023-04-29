package dev.mslalith.focuslauncher.core.testing.compose.assertion

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import dev.mslalith.focuslauncher.core.model.SelectedHiddenApp
import dev.mslalith.focuslauncher.core.testing.compose.matcher.hasSelectedHiddenApp

fun SemanticsNodeInteraction.assertSelectedHiddenApp(
    selectedHiddenApp: SelectedHiddenApp
): SemanticsNodeInteraction = assert(matcher = hasSelectedHiddenApp(selectedHiddenApp = selectedHiddenApp))
