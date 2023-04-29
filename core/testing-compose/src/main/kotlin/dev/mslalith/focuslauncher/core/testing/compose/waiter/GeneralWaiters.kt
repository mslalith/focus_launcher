package dev.mslalith.focuslauncher.core.testing.compose.waiter

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import dev.mslalith.focuslauncher.core.model.SelectedHiddenApp
import dev.mslalith.focuslauncher.core.testing.compose.matcher.hasSelectedHiddenApp

@OptIn(ExperimentalTestApi::class)
fun ComposeTestRule.waitForTag(
    testTag: String,
    timeoutMillis: Long = 5_000L
) {
    waitUntilNodeCount(
        matcher = hasTestTag(testTag = testTag),
        count = 1,
        timeoutMillis = timeoutMillis
    )
}

@OptIn(ExperimentalTestApi::class)
fun ComposeTestRule.waitForApp(
    selectedHiddenApp: SelectedHiddenApp,
    timeoutMillis: Long = 5_000L
) {
    waitUntilNodeCount(
        matcher = hasSelectedHiddenApp(selectedHiddenApp = selectedHiddenApp),
        count = 1,
        timeoutMillis = timeoutMillis
    )
}
