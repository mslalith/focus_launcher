package dev.mslalith.focuslauncher.core.testing.compose.waiter

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import dev.mslalith.focuslauncher.core.model.WidgetType
import dev.mslalith.focuslauncher.core.model.app.SelectedApp
import dev.mslalith.focuslauncher.core.model.app.SelectedHiddenApp
import dev.mslalith.focuslauncher.core.testing.compose.matcher.hasSelectedApp
import dev.mslalith.focuslauncher.core.testing.compose.matcher.hasSelectedHiddenApp
import dev.mslalith.focuslauncher.core.testing.compose.matcher.hasWidgetType

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
    selectedApp: SelectedApp,
    timeoutMillis: Long = 5_000L
) {
    waitUntilNodeCount(
        matcher = hasSelectedApp(selectedApp = selectedApp),
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

@OptIn(ExperimentalTestApi::class)
fun ComposeTestRule.waitForWidgetType(
    widgetType: WidgetType,
    timeoutMillis: Long = 5_000L
) {
    waitUntilNodeCount(
        matcher = hasWidgetType(widgetType = widgetType),
        count = 1,
        timeoutMillis = timeoutMillis
    )
}
