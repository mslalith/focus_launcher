package dev.mslalith.focuslauncher.core.testing.compose.matcher

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction

fun SemanticsNodeInteraction.onMatchWith(
    matcher: SemanticsMatcher
): Boolean = matcher.matches(node = fetchSemanticsNode())
