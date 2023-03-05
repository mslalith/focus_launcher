package dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.impl.TestSemanticsScopeImpl

fun Modifier.testSemantics(
    tag: String,
    block: TestSemanticsScope.() -> Unit = {}
): Modifier = this then testTag(tag = tag) then semantics {
    TestSemanticsScopeImpl(semanticsPropertyReceiver = this).block()
}
