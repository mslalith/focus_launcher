package dev.mslalith.focuslauncher.core.testing.compose

import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.semantics.SemanticsPropertyKey
import dev.mslalith.focuslauncher.core.model.app.SelectedApp
import dev.mslalith.focuslauncher.core.model.app.SelectedHiddenApp

object TestSemanticsProperties {
    val BiasAlignmentHorizontal = SemanticsPropertyKey<BiasAlignment.Horizontal>(name = "BiasAlignmentHorizontal")
    val BiasAlignmentVertical = SemanticsPropertyKey<BiasAlignment.Vertical>(name = "BiasAlignmentVertical")

    val SelectedApp = SemanticsPropertyKey<SelectedApp>(name = "SelectedApp")
    val SelectedHiddenApp = SemanticsPropertyKey<SelectedHiddenApp>(name = "SelectedHiddenApp")
}
