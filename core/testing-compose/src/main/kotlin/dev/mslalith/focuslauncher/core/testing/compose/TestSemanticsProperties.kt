package dev.mslalith.focuslauncher.core.testing.compose

import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.semantics.SemanticsPropertyKey
import dev.mslalith.focuslauncher.core.model.WidgetType
import dev.mslalith.focuslauncher.core.model.app.SelectedApp
import dev.mslalith.focuslauncher.core.model.app.SelectedHiddenApp

object TestSemanticsProperties {
    val StringType = SemanticsPropertyKey<String>(name = "StringType")
    val BooleanType = SemanticsPropertyKey<Boolean>(name = "BooleanType")

    val BiasAlignmentHorizontal = SemanticsPropertyKey<BiasAlignment.Horizontal>(name = "BiasAlignmentHorizontal")
    val BiasAlignmentVertical = SemanticsPropertyKey<BiasAlignment.Vertical>(name = "BiasAlignmentVertical")

    val SelectedApp = SemanticsPropertyKey<SelectedApp>(name = "SelectedApp")
    val SelectedHiddenApp = SemanticsPropertyKey<SelectedHiddenApp>(name = "SelectedHiddenApp")

    val WidgetType = SemanticsPropertyKey<WidgetType>(name = "WidgetType")
}
