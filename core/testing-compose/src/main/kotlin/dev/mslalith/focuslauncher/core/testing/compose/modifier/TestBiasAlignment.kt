package dev.mslalith.focuslauncher.core.testing.compose.modifier

import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import dev.mslalith.focuslauncher.core.testing.compose.TestSemanticsProperties

inline fun Modifier.testBiasAlignment(
    biasAlignment: BiasAlignment.Horizontal
): Modifier = then(semantics { this[TestSemanticsProperties.BiasAlignmentHorizontal] = biasAlignment })

inline fun Modifier.testBiasAlignment(
    biasAlignment: BiasAlignment.Vertical
): Modifier = then(semantics { this[TestSemanticsProperties.BiasAlignmentVertical] = biasAlignment })
