package dev.mslalith.focuslauncher.core.testing.compose

import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.semantics.SemanticsPropertyKey

object TestSemanticsProperties {
    val BiasAlignmentHorizontal = SemanticsPropertyKey<BiasAlignment.Horizontal>("BiasAlignmentHorizontal")
    val BiasAlignmentVertical = SemanticsPropertyKey<BiasAlignment.Vertical>("BiasAlignmentVertical")
}
