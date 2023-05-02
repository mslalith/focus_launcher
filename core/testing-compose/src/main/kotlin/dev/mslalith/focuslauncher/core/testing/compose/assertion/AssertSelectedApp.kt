package dev.mslalith.focuslauncher.core.testing.compose.assertion

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import dev.mslalith.focuslauncher.core.model.app.SelectedApp
import dev.mslalith.focuslauncher.core.testing.compose.matcher.hasSelectedApp

fun SemanticsNodeInteraction.assertSelectedApp(
    selectedApp: SelectedApp
): SemanticsNodeInteraction = assert(matcher = hasSelectedApp(selectedApp = selectedApp))
