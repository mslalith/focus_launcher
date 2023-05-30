package dev.mslalith.focuslauncher.core.testing.compose.waiter

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import dev.mslalith.focuslauncher.core.testing.compose.matcher.hasBoolean
import dev.mslalith.focuslauncher.core.testing.compose.matcher.hasString

context (ComposeContentTestRule)
@OptIn(ExperimentalTestApi::class)
fun SemanticsNodeInteraction.waitForString(
    value: String,
    timeoutMillis: Long = 5_000L
) {
    waitUntilAtLeastOneExists(
        matcher = hasString(value = value),
        timeoutMillis = timeoutMillis
    )
}

context (ComposeContentTestRule)
@OptIn(ExperimentalTestApi::class)
fun SemanticsNodeInteraction.waitForBoolean(
    value: Boolean,
    timeoutMillis: Long = 5_000L
) {
    waitUntilAtLeastOneExists(
        matcher = hasBoolean(value = value),
        timeoutMillis = timeoutMillis
    )
}
