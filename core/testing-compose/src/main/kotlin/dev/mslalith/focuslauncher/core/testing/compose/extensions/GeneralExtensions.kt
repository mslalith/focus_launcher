package dev.mslalith.focuslauncher.core.testing.compose.extensions

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.printToString

fun SemanticsNodeInteractionCollection.printToConsole(maxDepth: Int = 100) {
    printToString(maxDepth = maxDepth).let(::println)
}

fun SemanticsNodeInteraction.printToConsole(maxDepth: Int = 100) {
    printToString(maxDepth = maxDepth).let(::println)
}

fun SemanticsNodeInteraction.performScrollToAndClick() {
    performScrollTo()
    performClick()
}
