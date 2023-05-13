package dev.mslalith.focuslauncher.core.testing.compose.assertion

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText

@OptIn(ExperimentalTestApi::class)
fun ComposeTestRule.waitForTextAndAssertIsDisplayed(
    text: String,
    timeoutMillis: Long = 5_000L
) {
    waitUntilExactlyOneExists(
        matcher = hasText(text = text),
        timeoutMillis = timeoutMillis
    )
    onNodeWithText(text = text).assertIsDisplayed()
}

@OptIn(ExperimentalTestApi::class)
fun ComposeTestRule.waitForTagAndAssertIsDisplayed(
    testTag: String,
    timeoutMillis: Long = 5_000L
) {
    waitUntilExactlyOneExists(
        matcher = hasTestTag(testTag = testTag),
        timeoutMillis = timeoutMillis
    )
    onNodeWithTag(testTag = testTag).assertIsDisplayed()
}
