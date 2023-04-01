package dev.mslalith.focuslauncher.core.testing.compose

import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.semantics.SemanticsPropertyKey
import dev.mslalith.focuslauncher.core.model.SelectedApp

object TestSemanticsProperties {
    val BiasAlignmentHorizontal = SemanticsPropertyKey<BiasAlignment.Horizontal>("BiasAlignmentHorizontal")
    val BiasAlignmentVertical = SemanticsPropertyKey<BiasAlignment.Vertical>("BiasAlignmentVertical")

    val SelectedApp = SemanticsPropertyKey<SelectedApp>("SelectedApp")
}
